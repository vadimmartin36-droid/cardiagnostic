package com.example.network

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

data class DiagnosticResult(
    val technicalSummary: String,
    val plainExplanation: String,
    val severity: String, // "GREEN", "YELLOW", "RED"
    val severityTitle: String,
    val estimatedCostRange: String,
    val isDiy: Boolean,
    val diyInstructions: List<String>,
    val diyVideoQuery: String,
    val urgencyAdvice: String,
    val recommendedAction: String
)

class GeminiDiagnosticService(private val context: Context) {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private fun Bitmap.toBase64(): String {
        val outputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }

    suspend fun analyzeVehicleIssue(
        carInfo: String,
        symptomText: String?,
        imageBitmap: Bitmap?,
        appLanguage: com.example.ui.AppLanguage = com.example.ui.AppLanguage.RU
    ): DiagnosticResult = withContext(Dispatchers.IO) {
        val buildConfigKey = BuildConfig.GEMINI_API_KEY
        val envKey = System.getenv("GEMINI_API_KEY") ?: ""
        val apiKey = if (buildConfigKey.isNotBlank() && buildConfigKey != "MY_GEMINI_API_KEY") {
            buildConfigKey
        } else if (envKey.isNotBlank() && envKey != "MY_GEMINI_API_KEY") {
            envKey
        } else {
            ""
        }

        if (apiKey.isEmpty()) {
            return@withContext generateFallbackDiagnosis(carInfo, symptomText, imageBitmap != null, appLanguage)
        }

        val langInstruction = when (appLanguage) {
            com.example.ui.AppLanguage.RU -> "CRITICAL REQUIREMENT: Write ALL string field values in fluent, highly accurate, technical Russian language (русский язык) for a car owner. Ensure trouble codes, fault descriptions, severity titles, repair advice, step-by-step DIY instructions are completely in Russian without any untranslated English phrases. Prices and estimated repair costs MUST ALWAYS be written strictly in US Dollars $ (e.g. '$150 - $350')."
            com.example.ui.AppLanguage.PL -> "CRITICAL REQUIREMENT: Write ALL string field values in fluent, highly accurate, technical Polish language (język polski) for a car owner. Ensure trouble codes, fault descriptions, severity titles, repair advice, step-by-step DIY instructions are completely in Polish without any untranslated English phrases. Prices and estimated repair costs MUST ALWAYS be written strictly in US Dollars $ (e.g. '$150 - $350')."
            com.example.ui.AppLanguage.EN -> "Write all string field values in clear English. Prices and estimated repair costs MUST ALWAYS be written strictly in US Dollars $ (e.g. '$150 - $350')."
            com.example.ui.AppLanguage.UA -> "CRITICAL REQUIREMENT: Write ALL string field values in fluent, highly accurate, technical Ukrainian language (українська мова) for a car owner. Ensure trouble codes, fault descriptions, severity titles, repair advice, step-by-step DIY instructions are completely in Ukrainian without any untranslated English phrases. Prices and estimated repair costs MUST ALWAYS be written strictly in US Dollars $ (e.g. '$150 - $350')."
        }

        val systemPrompt = """
            You are an expert Automotive Diagnostic AI Assistant specializing in non-OBD visual warning light recognition, symptom analysis, and car mechanics.
            
            CRITICAL VEHICLE CONTEXT:
            The user is asking about their specific vehicle: "$carInfo".
            Your entire analysis MUST be specifically tailored to this exact vehicle ($carInfo).
            Consider the vehicle make, model, year, engine type, and current mileage when determining:
            - Typical mechanical failure points and common issues for this model
            - Specific trouble codes (e.g. P0300, P0420, P0171, ABS codes) likely for $carInfo
            - Precise step-by-step DIY instructions applicable to $carInfo
            - Estimated repair costs in USD $ tailored for this vehicle segment
            
            $langInstruction
            
            You MUST return ONLY a strict JSON object with the following fields:
            {
              "technicalSummary": "Short mechanical summary with trouble codes specifically for $carInfo",
              "plainExplanation": "2-3 simple sentences explaining what is wrong with this $carInfo and why it happens",
              "severity": "GREEN" or "YELLOW" or "RED",
              "severityTitle": "Clear single line advice starting with indicator emoji (e.g., 🟢 Безопасно для езды... OR 🟡 Внимание... OR 🔴 Опасно!...)",
              "estimatedCostRange": "Price range estimate strictly in US Dollars $ (e.g., '$150 - $350' or '$1,200 - $2,500')",
              "isDiy": true or false,
              "diyInstructions": ["Step 1...", "Step 2...", "Step 3..."],
              "diyVideoQuery": "Short YouTube search query phrase for DIY fix on $carInfo",
              "urgencyAdvice": "Specific driveability guidance for the car owner",
              "recommendedAction": "Actionable next step advice"
            }
        """.trimIndent()

        val partsArray = JSONArray()

        val promptText = buildString {
            append("Analyze this car issue. ")
            if (!symptomText.isNullOrBlank()) {
                append("Reported symptoms/description: \"").append(symptomText).append("\". ")
            }
            if (imageBitmap != null) {
                append("A photo of the dashboard warning light or engine component is attached. Examine symbols, lights, or visible damage.")
            }
        }
        partsArray.put(JSONObject().put("text", promptText))

        if (imageBitmap != null) {
            val imagePart = JSONObject().apply {
                put("inlineData", JSONObject().apply {
                    put("mimeType", "image/jpeg")
                    put("data", imageBitmap.toBase64())
                })
            }
            partsArray.put(imagePart)
        }

        val contentsArray = JSONArray().apply {
            put(JSONObject().put("parts", partsArray))
        }

        val requestJson = JSONObject().apply {
            put("contents", contentsArray)
            put("systemInstruction", JSONObject().put("parts", JSONArray().put(JSONObject().put("text", systemPrompt))))
            put("generationConfig", JSONObject().apply {
                put("responseMimeType", "application/json")
                put("temperature", 0.2)
            })
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
        val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response = okHttpClient.newCall(request).execute()
            val responseString = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext generateFallbackDiagnosis(carInfo, symptomText, imageBitmap != null, appLanguage)
            }

            val responseObj = JSONObject(responseString)
            val candidates = responseObj.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val contentObj = firstCandidate?.optJSONObject("content")
            val parts = contentObj?.optJSONArray("parts")
            val textOutput = parts?.optJSONObject(0)?.optString("text") ?: ""

            return@withContext parseJsonResult(textOutput, carInfo, symptomText, imageBitmap != null, appLanguage)
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext generateFallbackDiagnosis(carInfo, symptomText, imageBitmap != null, appLanguage)
        }
    }

    private fun parseJsonResult(
        jsonString: String,
        carInfo: String,
        symptomText: String?,
        hasImage: Boolean,
        appLanguage: com.example.ui.AppLanguage
    ): DiagnosticResult {
        return try {
            val cleanJson = jsonString.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
            val json = JSONObject(cleanJson)

            val defaultTech = when (appLanguage) {
                com.example.ui.AppLanguage.RU -> "Индикатор панели / Сигнал системы"
                com.example.ui.AppLanguage.PL -> "Kontrolka / Sygnał systemu"
                com.example.ui.AppLanguage.EN -> "Dashboard Indicator / System Alert"
                com.example.ui.AppLanguage.UA -> "Індикатор панелі / Сигнал системи"
            }
            val defaultPlain = when (appLanguage) {
                com.example.ui.AppLanguage.RU -> "Система автомобиля зафиксировала сбой, требующий внимания."
                com.example.ui.AppLanguage.PL -> "System samochodu wykrył usterkę wymagającą uwagi."
                com.example.ui.AppLanguage.EN -> "Your vehicle system detected an abnormality that requires attention."
                com.example.ui.AppLanguage.UA -> "Система автомобіля зафіксувала збій, що вимагає уваги."
            }

            val technical = json.optString("technicalSummary", defaultTech)
            val plain = json.optString("plainExplanation", defaultPlain)
            val severityStr = json.optString("severity", "YELLOW").uppercase()
            val severity = when (severityStr) {
                "GREEN" -> "GREEN"
                "RED" -> "RED"
                else -> "YELLOW"
            }
            val defaultSeverityTitle = when (severity) {
                "GREEN" -> when (appLanguage) {
                    com.example.ui.AppLanguage.RU -> "🟢 Безопасно для езды. Запланируйте проверку."
                    com.example.ui.AppLanguage.PL -> "🟢 Bezpiecznie. Zaplanuj przegląd."
                    com.example.ui.AppLanguage.EN -> "🟢 Safe to drive. Schedule a check-up."
                    com.example.ui.AppLanguage.UA -> "🟢 Безпечно для їзди. Заплануйте перевірку."
                }
                "RED" -> when (appLanguage) {
                    com.example.ui.AppLanguage.RU -> "🔴 Опасно! Немедленно остановите движение."
                    com.example.ui.AppLanguage.PL -> "🔴 Niebezpieczeństwo! Natychmiast zatrzymaj pojazd."
                    com.example.ui.AppLanguage.EN -> "🔴 Danger! Stop the car immediately."
                    com.example.ui.AppLanguage.UA -> "🔴 Небезпечно! Зупиніть автомобіль негайно."
                }
                else -> when (appLanguage) {
                    com.example.ui.AppLanguage.RU -> "🟡 Внимание. Требуется диагностика."
                    com.example.ui.AppLanguage.PL -> "🟡 Uwaga. Zaplanuj kontrolę."
                    com.example.ui.AppLanguage.EN -> "🟡 Caution. Get it checked soon."
                    com.example.ui.AppLanguage.UA -> "🟡 Увага. Потрібна діагностика."
                }
            }
            val severityTitle = json.optString("severityTitle", defaultSeverityTitle)
            val cost = json.optString("estimatedCostRange", "$120 - $350")
            val isDiy = json.optBoolean("isDiy", false)
            val diyList = mutableListOf<String>()
            val diyArr = json.optJSONArray("diyInstructions")
            if (diyArr != null) {
                for (i in 0 until diyArr.length()) {
                    diyList.add(diyArr.getString(i))
                }
            } else {
                diyList.add(when (appLanguage) {
                    com.example.ui.AppLanguage.RU -> "Проверьте уровень технических жидкостей и затяжку крышки."
                    com.example.ui.AppLanguage.PL -> "Sprawdź poziom płynów eksploatacyjnych i dokręcenie korka."
                    com.example.ui.AppLanguage.EN -> "Inspect fluid levels and tighten cap."
                    com.example.ui.AppLanguage.UA -> "Перевірте рівень технічних рідин та затяжку кришки."
                })
            }
            val videoQuery = json.optString("diyVideoQuery", "How to fix $carInfo $symptomText")
            val urgency = json.optString("urgencyAdvice", when (appLanguage) {
                com.example.ui.AppLanguage.RU -> "Проведите проверку в течение ближайших 100 км."
                com.example.ui.AppLanguage.PL -> "Wykonaj przegląd w ciągu najbliższych 100 km."
                com.example.ui.AppLanguage.EN -> "Have a mechanic inspect within 100 km."
                com.example.ui.AppLanguage.UA -> "Проведіть перевірку протягом найближчих 100 км."
            })
            val action = json.optString("recommendedAction", when (appLanguage) {
                com.example.ui.AppLanguage.RU -> "Запланируйте визит в проверенный автосервис."
                com.example.ui.AppLanguage.PL -> "Zaplanuj wizytę w serwisie."
                com.example.ui.AppLanguage.EN -> "Schedule an inspection at a local certified service center."
                com.example.ui.AppLanguage.UA -> "Заплануйте візит до перевіреного автосервісу."
            })

            DiagnosticResult(
                technicalSummary = technical,
                plainExplanation = plain,
                severity = severity,
                severityTitle = severityTitle,
                estimatedCostRange = cost,
                isDiy = isDiy,
                diyInstructions = diyList,
                diyVideoQuery = videoQuery,
                urgencyAdvice = urgency,
                recommendedAction = action
            )
        } catch (e: Exception) {
            generateFallbackDiagnosis(carInfo, symptomText, hasImage, appLanguage)
        }
    }

    private fun generateFallbackDiagnosis(
        carInfo: String,
        symptomText: String?,
        hasImage: Boolean,
        appLanguage: com.example.ui.AppLanguage
    ): DiagnosticResult {
        val symptom = symptomText?.lowercase() ?: ""
        val symptomLabel = symptomText?.trim()?.ifBlank { null }

        val isRussian = appLanguage == com.example.ui.AppLanguage.RU
        val isPolish = appLanguage == com.example.ui.AppLanguage.PL
        val isUkrainian = appLanguage == com.example.ui.AppLanguage.UA

        if (isUkrainian) {
            val userTextDesc = symptomLabel?.let { " за запитом «$it»" } ?: ""
            return when {
                symptom.contains("гальм") || symptom.contains("скрип") || symptom.contains("писк") || symptom.contains("гальмув") -> DiagnosticResult(
                    technicalSummary = "Знос гальмівної системи $carInfo / Колодки та диски",
                    plainExplanation = "На $carInfo зафіксовано ознаки зносу фрикційного шару гальмівних колодок або деформації гальмівного диска$userTextDesc.",
                    severity = "YELLOW",
                    severityTitle = "🟡 Увага. Перевірте гальма $carInfo.",
                    estimatedCostRange = "$150 - $350",
                    isDiy = false,
                    diyInstructions = listOf(
                        "Огляньте фрикційний шар колодок на $carInfo через колісний диск.",
                        "Перевірте рівень гальмівної рідини під капотом.",
                        "Заплануйте заміну колодок та дисків в автосервісі."
                    ),
                    diyVideoQuery = "Заміна гальмівних колодок $carInfo",
                    urgencyAdvice = "Дотримуйтесь збільшеної дистанції під час поїздок.",
                    recommendedAction = "Зверніться до СТО для огляду гальмівної системи $carInfo."
                )
                symptom.contains("мастил") || symptom.contains("масл") || symptom.contains("витік") || symptom.contains("тиск") -> DiagnosticResult(
                    technicalSummary = "Тиск / Рівень моторного мастила $carInfo",
                    plainExplanation = "Для $carInfo зафіксовано сигнал про можливе зниження тиску або рівня моторного мастила нижче норми$userTextDesc.",
                    severity = "RED",
                    severityTitle = "🔴 Небезпечно! Негайно вимкніть двигун.",
                    estimatedCostRange = "$40 - $120 (Мастило та фільтр)",
                    isDiy = true,
                    diyInstructions = listOf(
                        "Вимкніть $carInfo і дайте мотору охолонути 5 хвилин.",
                        "Перевірте рівень мастила щупом і долийте відповідне мастило.",
                        "Огляньте підкапотний простір на наявність підтікань."
                    ),
                    diyVideoQuery = "Як долити мастило $carInfo",
                    urgencyAdvice = "Не експлуатуйте $carInfo з низьким рівнем мастила.",
                    recommendedAction = "Долийте мастило до норми перед рухом."
                )
                symptom.contains("акум") || symptom.contains("батарей") || symptom.contains("завод") || symptom.contains("клацан") -> DiagnosticResult(
                    technicalSummary = "Заряд АКБ / Пусковий ланцюг $carInfo",
                    plainExplanation = "На $carInfo спостерігається просідання напруги акумулятора або проблеми з генератором/стартером$userTextDesc.",
                    severity = "YELLOW",
                    severityTitle = "🟡 Увага. Перевірте АКБ на $carInfo.",
                    estimatedCostRange = "$100 - $250",
                    isDiy = true,
                    diyInstructions = listOf(
                        "Очистіть клеми АКБ на $carInfo від окислення.",
                        "Заміряйте напругу мультиметром (норма 12.6V).",
                        "Перевірте генератор під навантаженням."
                    ),
                    diyVideoQuery = "Перевірка АКБ та генератора $carInfo",
                    urgencyAdvice = "Уникайте глушіння двигуна далеко від сервісу.",
                    recommendedAction = "Перевірте пусковий струм батареї в автомагазині."
                )
                !symptomLabel.isNullOrBlank() -> DiagnosticResult(
                    technicalSummary = "Діагностичний звіт $carInfo: $symptomLabel",
                    plainExplanation = "За вашим описом «$symptomLabel» для $carInfo потрібна перевірка паливної, впускної та електричної систем.",
                    severity = "YELLOW",
                    severityTitle = "🟡 Увага. Потрібен огляд вузлів $carInfo.",
                    estimatedCostRange = "$90 - $280",
                    isDiy = true,
                    diyInstructions = listOf(
                        "Перевірте відсутність зовнішніх пошкоджень та підтікань на $carInfo.",
                        "Зчитайте коди помилок сканером OBD2 за наявності.",
                        "Перевірте свічки запалювання та фільтри."
                    ),
                    diyVideoQuery = "Ремонт та діагностика $carInfo $symptomLabel",
                    urgencyAdvice = "Проведіть огляд перед довгою поїздкою.",
                    recommendedAction = "Заплануйте візит на СТО для діагностики $carInfo."
                )
                hasImage -> DiagnosticResult(
                    technicalSummary = "Індикатор Check Engine / Помилка систем $carInfo",
                    plainExplanation = "На приладовій панелі $carInfo зафіксовано попереджувальний значок. Потрібне зчитування кодів OBD2.",
                    severity = "YELLOW",
                    severityTitle = "🟡 Увага. Комп'ютерна діагностика $carInfo.",
                    estimatedCostRange = "$120 - $350",
                    isDiy = true,
                    diyInstructions = listOf(
                        "Перевірте щільність кришки бензобака на $carInfo.",
                        "Огляньте стан повітряного фільтра.",
                        "Зчитайте збережений код P0420/P0300 через OBD2 сканер."
                    ),
                    diyVideoQuery = "Горить чек енджин $carInfo",
                    urgencyAdvice = "При рівній роботі ДВЗ можна доїхати до СТО.",
                    recommendedAction = "Зчитайте помилки діагностичним сканером."
                )
                else -> DiagnosticResult(
                    technicalSummary = "Комплексна перевірка $carInfo",
                    plainExplanation = "Для $carInfo виконано первинну перевірку. Усі основні вузли вимагають планового контролю рідин.",
                    severity = "GREEN",
                    severityTitle = "🟢 Автомобіль $carInfo готовий до поїздок.",
                    estimatedCostRange = "$60 - $180",
                    isDiy = true,
                    diyInstructions = listOf(
                        "Перевірте тиск у шинах $carInfo.",
                        "Перевірте рівень охолоджувальної та омивальної рідин.",
                        "Зафіксуйте поточний пробіг."
                    ),
                    diyVideoQuery = "Обслуговування $carInfo",
                    urgencyAdvice = "Автомобіль готовий до стандартної експлуатації.",
                    recommendedAction = "Заплануйте плановий візит на ТО."
                )
            }
        }

        if (isPolish) {
            val userTextDesc = symptomLabel?.let { " dla «$it»" } ?: ""
            return when {
                symptom.contains("hamulec") || symptom.contains("pisz") || symptom.contains("pisk") || symptom.contains("hamow") -> DiagnosticResult(
                    technicalSummary = "Zużycie układu hamulcowego $carInfo / Klocki i tarcze",
                    plainExplanation = "W $carInfo Wykryto objawy zużycia klocków hamulcowych lub odkształcenia tarczy$userTextDesc.",
                    severity = "YELLOW",
                    severityTitle = "🟡 Uwaga. Sprawdź hamulce $carInfo.",
                    estimatedCostRange = "$150 - $350",
                    isDiy = false,
                    diyInstructions = listOf(
                        "Sprawdź grubość klocków w $carInfo przez felgę.",
                        "Sprawdź poziom płynu hamulcowego pod maską.",
                        "Zaplanuj wymianę klocków i tarcz w serwisie."
                    ),
                    diyVideoQuery = "Wymiana klocków hamulcowych $carInfo",
                    urgencyAdvice = "Zachowaj większą odległość podczas jazdy.",
                    recommendedAction = "Skonsultuj się z serwisem w celu kontroli układu hamulcowego $carInfo."
                )
                symptom.contains("olej") || symptom.contains("wyciek") || symptom.contains("ciśnien") -> DiagnosticResult(
                    technicalSummary = "Ciśnienie / Poziom oleju silnikowego $carInfo",
                    plainExplanation = "Dla $carInfo Wykryto sygnał o możliwym spadku ciśnienia lub poziomu oleju silnikowego poniżej normy$userTextDesc.",
                    severity = "RED",
                    severityTitle = "🔴 Niebezpieczeństwo! Natychmiast wyłącz silnik.",
                    estimatedCostRange = "$40 - $120 (Olej i filtr)",
                    isDiy = true,
                    diyInstructions = listOf(
                        "Wyłącz $carInfo i odczekaj 5 minut aż silnik ostygnie.",
                        "Sprawdź poziom oleju bagietką i uzupełnij odpowiedni olej.",
                        "Skontroluj komorę silnika pod kątem wycieków."
                    ),
                    diyVideoQuery = "Jak dolać olej $carInfo",
                    urgencyAdvice = "Nie jeździj $carInfo z niskim poziomem oleju.",
                    recommendedAction = "Uzgodnij poziom oleju przed dalszą jazdą."
                )
                else -> DiagnosticResult(
                    technicalSummary = "Raport diagnostyczny $carInfo${if (!symptomLabel.isNullOrBlank()) ": $symptomLabel" else ""}",
                    plainExplanation = "Na podstawie zgłoszenia dla $carInfo zalecana jest kontrola czujników oraz podzespołów silnika.",
                    severity = "YELLOW",
                    severityTitle = "🟡 Uwaga. Sprawdź układy $carInfo.",
                    estimatedCostRange = "$120 - $320",
                    isDiy = true,
                    diyInstructions = listOf(
                        "Sprawdź poziom płynów pod maską $carInfo.",
                        "Odczytaj kody błędów skanerem OBD2.",
                        "Sprawdź filtry powietrza i paliwa."
                    ),
                    diyVideoQuery = "Diagnostyka $carInfo $symptom",
                    urgencyAdvice = "Obserwuj pracę samochodu.",
                    recommendedAction = "Zaplanuj wizytę w serwisie."
                )
            }
        }

        if (isRussian) {
            val userTextDesc = symptomLabel?.let { " по запросу «$it»" } ?: ""
            return when {
                symptom.contains("тормоз") || symptom.contains("скрип") || symptom.contains("писк") || symptom.contains("торможен") -> DiagnosticResult(
                    technicalSummary = "Износ тормозной системы $carInfo / Колодки и диски",
                    plainExplanation = "На $carInfo зафиксированы признаки износа фрикционного слоя тормозных колодок или деформации тормозного диска$userTextDesc.",
                    severity = "YELLOW",
                    severityTitle = "🟡 Внимание. Проверьте тормоза $carInfo.",
                    estimatedCostRange = "$150 - $350",
                    isDiy = false,
                    diyInstructions = listOf(
                        "Осмотрите фрикционный слой колодок на $carInfo через колесный диск.",
                        "Проверьте уровень тормозной жидкости под капотом.",
                        "Запланируйте замену колодок и дисков в автосервисе."
                    ),
                    diyVideoQuery = "Замена тормозных колодок $carInfo",
                    urgencyAdvice = "Соблюдайте увеличенную дистанцию во время поездок.",
                    recommendedAction = "Обратитесь в СТО для осмотра тормозной системы $carInfo."
                )
                symptom.contains("масл") || symptom.contains("утек") || symptom.contains("давлен") -> DiagnosticResult(
                    technicalSummary = "Давление / Уровень моторного масла $carInfo",
                    plainExplanation = "Для $carInfo зафиксирован сигнал о возможном снижении давления или уровня моторного масла ниже нормы$userTextDesc.",
                    severity = "RED",
                    severityTitle = "🔴 Опасно! Заглушите двигатель немедленно.",
                    estimatedCostRange = "$40 - $120 (Масло и фильтр)",
                    isDiy = true,
                    diyInstructions = listOf(
                        "Заглушите $carInfo и дайте мотору остыть 5 минут.",
                        "Проверьте уровень масла щупом и долейте подходящее масло.",
                        "Осмотрите подкапотное пространство на предмет подтеков."
                    ),
                    diyVideoQuery = "Как долить масло $carInfo",
                    urgencyAdvice = "Не эксплуатируйте $carInfo с низким уровнем масла.",
                    recommendedAction = "Долейте масло до нормы перед движением."
                )
                symptom.contains("аккум") || symptom.contains("батаре") || symptom.contains("завод") || symptom.contains("щелч") -> DiagnosticResult(
                    technicalSummary = "Заряд АКБ / Пусковая цепь $carInfo",
                    plainExplanation = "На $carInfo наблюдается просадка напряжения аккумулятора или проблемы с генератором/стартером$userTextDesc.",
                    severity = "YELLOW",
                    severityTitle = "🟡 Внимание. Проверьте АКБ на $carInfo.",
                    estimatedCostRange = "$100 - $250",
                    isDiy = true,
                    diyInstructions = listOf(
                        "Очистите клеммы АКБ на $carInfo от окисления.",
                        "Замерьте напряжение мультиметром (норма 12.6V).",
                        "Проверьте генератор под нагрузкой."
                    ),
                    diyVideoQuery = "Проверка АКБ и генератора $carInfo",
                    urgencyAdvice = "Избегайте глушить двигатель вдали от сервиса.",
                    recommendedAction = "Проверьте пусковой ток батареи в автомагазине."
                )
                !symptomLabel.isNullOrBlank() -> DiagnosticResult(
                    technicalSummary = "Диагностический отчет $carInfo: $symptomLabel",
                    plainExplanation = "По вашему описанию «$symptomLabel» для $carInfo требуется проверка топливной, впускной и электрической систем.",
                    severity = "YELLOW",
                    severityTitle = "🟡 Внимание. Требуется осмотр узлов $carInfo.",
                    estimatedCostRange = "$90 - $280",
                    isDiy = true,
                    diyInstructions = listOf(
                        "Проверьте отсутствие внешних повреждений и подтеков на $carInfo.",
                        "Считайте коды ошибок сканером OBD2 при наличии.",
                        "Проверьте свечи зажигания и фильтра."
                    ),
                    diyVideoQuery = "Ремонт и диагностика $carInfo $symptomLabel",
                    urgencyAdvice = "Проведите осмотр перед долгой поездкой.",
                    recommendedAction = "Запланируйте визит на СТО для диагностики $carInfo."
                )
                hasImage -> DiagnosticResult(
                    technicalSummary = "Индикатор Check Engine / Ошибка систем $carInfo",
                    plainExplanation = "На приборной панели $carInfo зафиксирован предупреждающий значок. Требуется считывание кодов OBD2.",
                    severity = "YELLOW",
                    severityTitle = "🟡 Внимание. Компьютерная диагностика $carInfo.",
                    estimatedCostRange = "$120 - $350",
                    isDiy = true,
                    diyInstructions = listOf(
                        "Проверьте плотность крышки бензобака на $carInfo.",
                        "Осмотрите состояние воздушного фильтра.",
                        "Считайте сохраненный код P0420/P0300 через OBD2 сканер."
                    ),
                    diyVideoQuery = "Горит чек энджин $carInfo",
                    urgencyAdvice = "При ровной работе ДВС можно доехать до СТО.",
                    recommendedAction = "Считайте ошибки диагностическим сканером."
                )
                else -> DiagnosticResult(
                    technicalSummary = "Комплексная проверка $carInfo",
                    plainExplanation = "Для $carInfo выполнена первичная проверка. Все основные узлы требуют планового контроля жидкостей.",
                    severity = "GREEN",
                    severityTitle = "🟢 Автомобиль $carInfo готов к поездкам.",
                    estimatedCostRange = "$60 - $180",
                    isDiy = true,
                    diyInstructions = listOf(
                        "Проверьте давление шин $carInfo.",
                        "Проверьте уровень охлаждающей и омывающей жидкостей.",
                        "Зафиксируйте текущий пробег."
                    ),
                    diyVideoQuery = "Обслуживание $carInfo",
                    urgencyAdvice = "Автомобиль готов к стандартной эксплуатации.",
                    recommendedAction = "Запланируйте плановый визит на ТО."
                )
            }
        }

        return DiagnosticResult(
            technicalSummary = "Diagnostic Summary for $carInfo${if (!symptomLabel.isNullOrBlank()) ": $symptomLabel" else ""}",
            plainExplanation = "Based on your input for $carInfo${if (!symptomLabel.isNullOrBlank()) " (\"$symptomLabel\")" else ""}, an inspection of sensor logic and powertrain components is recommended.",
            severity = "YELLOW",
            severityTitle = "🟡 Caution. Check $carInfo systems.",
            estimatedCostRange = "$120 - $320",
            isDiy = true,
            diyInstructions = listOf(
                "Inspect under-hood fluid levels on $carInfo.",
                "Scan for stored OBD2 trouble codes.",
                "Check air and fuel filters."
            ),
            diyVideoQuery = "How to inspect $carInfo $symptom",
            urgencyAdvice = "Monitor driving performance closely.",
            recommendedAction = "Schedule an inspection at a certified service center."
        )
    }
}
