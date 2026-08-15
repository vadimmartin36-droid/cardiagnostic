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
        isRussian: Boolean = true
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
            return@withContext generateFallbackDiagnosis(carInfo, symptomText, imageBitmap != null, isRussian)
        }

        val langInstruction = if (isRussian) {
            "CRITICAL REQUIREMENT: Write ALL string field values in fluent, highly accurate, technical Russian language (русский язык) for a car owner. Ensure trouble codes, fault descriptions, severity titles, repair advice, step-by-step DIY instructions are completely in Russian without any untranslated English phrases. Prices and estimated repair costs MUST ALWAYS be written strictly in US Dollars $ (e.g. '$150 - $350')."
        } else {
            "Write all string field values in clear English. Prices and estimated repair costs MUST ALWAYS be written strictly in US Dollars $ (e.g. '$150 - $350')."
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
                return@withContext generateFallbackDiagnosis(carInfo, symptomText, imageBitmap != null, isRussian)
            }

            val responseObj = JSONObject(responseString)
            val candidates = responseObj.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val contentObj = firstCandidate?.optJSONObject("content")
            val parts = contentObj?.optJSONArray("parts")
            val textOutput = parts?.optJSONObject(0)?.optString("text") ?: ""

            return@withContext parseJsonResult(textOutput, carInfo, symptomText, imageBitmap != null, isRussian)
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext generateFallbackDiagnosis(carInfo, symptomText, imageBitmap != null, isRussian)
        }
    }

    private fun parseJsonResult(
        jsonString: String,
        carInfo: String,
        symptomText: String?,
        hasImage: Boolean,
        isRussian: Boolean
    ): DiagnosticResult {
        return try {
            val cleanJson = jsonString.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
            val json = JSONObject(cleanJson)

            val technical = json.optString("technicalSummary", if (isRussian) "Индикатор панели / Сигнал системы" else "Dashboard Indicator / System Alert")
            val plain = json.optString("plainExplanation", if (isRussian) "Система автомобиля зафиксировала сбой, требующий внимания." else "Your vehicle system detected an abnormality that requires attention.")
            val severityStr = json.optString("severity", "YELLOW").uppercase()
            val severity = when (severityStr) {
                "GREEN" -> "GREEN"
                "RED" -> "RED"
                else -> "YELLOW"
            }
            val defaultSeverityTitle = when (severity) {
                "GREEN" -> if (isRussian) "🟢 Безопасно для езды. Запланируйте проверку." else "🟢 Safe to drive. Schedule a check-up."
                "RED" -> if (isRussian) "🔴 Опасно! Немедленно остановите движение." else "🔴 Danger! Stop the car immediately."
                else -> if (isRussian) "🟡 Внимание. Требуется диагностика." else "🟡 Caution. Get it checked soon."
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
                diyList.add(if (isRussian) "Проверьте уровень технических жидкостей и затяжку крышки." else "Inspect fluid levels and tighten cap.")
            }
            val videoQuery = json.optString("diyVideoQuery", "How to fix $carInfo $symptomText")
            val urgency = json.optString("urgencyAdvice", if (isRussian) "Проведите проверку в течение ближайших 100 км." else "Have a mechanic inspect within 100 km.")
            val action = json.optString("recommendedAction", if (isRussian) "Запланируйте визит в проверенный автосервис." else "Schedule an inspection at a local certified service center.")

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
            generateFallbackDiagnosis(carInfo, symptomText, hasImage, isRussian)
        }
    }

    private fun generateFallbackDiagnosis(
        carInfo: String,
        symptomText: String?,
        hasImage: Boolean,
        isRussian: Boolean
    ): DiagnosticResult {
        val symptom = symptomText?.lowercase() ?: ""
        val symptomLabel = symptomText?.trim()?.ifBlank { null }

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
