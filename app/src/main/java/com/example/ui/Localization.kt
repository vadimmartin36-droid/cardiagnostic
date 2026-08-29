package com.example.ui

enum class AppLanguage {
    RU, EN, PL, UA
}

object Localization {

    fun appTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "CarDiagnostic AI"
        AppLanguage.EN -> "CarDiagnostic AI"
        AppLanguage.PL -> "CarDiagnostic AI"
        AppLanguage.UA -> "CarDiagnostic AI"
    }

    fun appSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Умная автодиагностика"
        AppLanguage.EN -> "Smart Vehicle Diagnostics"
        AppLanguage.PL -> "Inteligentna Diagnostyka Samochodowa"
        AppLanguage.UA -> "Розумна автодіагностика"
    }

    fun appVersion(lang: AppLanguage) = "v.0.0.3"

    fun developerInfo(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Разработчик: VM"
        AppLanguage.EN -> "Developer: VM"
        AppLanguage.PL -> "Deweloper: VM"
        AppLanguage.UA -> "Розробник: VM"
    }

    fun freeScansBadge(lang: AppLanguage, count: Int) = when (lang) {
        AppLanguage.RU -> "$count/3 сканов"
        AppLanguage.EN -> "$count/3 Free Scans"
        AppLanguage.PL -> "$count/3 skanów"
        AppLanguage.UA -> "$count/3 сканувань"
    }

    fun proUnlimitedBadge(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "PRO БЕЗЛИМИТ"
        AppLanguage.EN -> "PRO UNLIMITED"
        AppLanguage.PL -> "PRO BEZ LIMITU"
        AppLanguage.UA -> "PRO БЕЗЛІМІТ"
    }

    fun garageLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Гараж"
        AppLanguage.EN -> "Garage"
        AppLanguage.PL -> "Garaż"
        AppLanguage.UA -> "Гараж"
    }

    fun addVehicleTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Добавить автомобиль"
        AppLanguage.EN -> "Add Your Vehicle"
        AppLanguage.PL -> "Dodaj pojazd"
        AppLanguage.UA -> "Додати автомобіль"
    }

    fun tapToConfigure(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Нажмите, чтобы настроить профиль"
        AppLanguage.EN -> "Tap to configure profile"
        AppLanguage.PL -> "Stuknij, aby skonfigurować profil"
        AppLanguage.UA -> "Торкніться, щоб налаштувати профіль"
    }

    // Nav Labels
    fun navHome(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Главная"
        AppLanguage.EN -> "Home"
        AppLanguage.PL -> "Główna"
        AppLanguage.UA -> "Головна"
    }

    fun navScan(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Сканер"
        AppLanguage.EN -> "Scan"
        AppLanguage.PL -> "Skaner"
        AppLanguage.UA -> "Сканер"
    }

    fun navHistory(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "История"
        AppLanguage.EN -> "History"
        AppLanguage.PL -> "Historia"
        AppLanguage.UA -> "Історія"
    }

    fun navGarage(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Гараж"
        AppLanguage.EN -> "Garage"
        AppLanguage.PL -> "Garaż"
        AppLanguage.UA -> "Гараж"
    }

    fun navPro(lang: AppLanguage, isPro: Boolean) = when (lang) {
        AppLanguage.RU -> "PRO"
        AppLanguage.EN -> if (isPro) "Pro" else "Get Pro"
        AppLanguage.PL -> if (isPro) "Pro" else "Kup Pro"
        AppLanguage.UA -> if (isPro) "Pro" else "Отримати Pro"
    }

    // HomeScreen
    fun recentDiagnoses(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "История проверок"
        AppLanguage.EN -> "Recent Diagnoses"
        AppLanguage.PL -> "Historia diagnostyk"
        AppLanguage.UA -> "Історія перевірок"
    }

    fun viewAll(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Все записи"
        AppLanguage.EN -> "View All"
        AppLanguage.PL -> "Wszystkie"
        AppLanguage.UA -> "Усі записи"
    }

    fun proUnlimited(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "PRO БЕЗЛИМИТ"
        AppLanguage.EN -> "PRO UNLIMITED"
        AppLanguage.PL -> "PRO BEZ LIMITU"
        AppLanguage.UA -> "PRO БЕЗЛІМІТ"
    }

    fun formatTimeRemaining(lang: AppLanguage, remainingMs: Long): String {
        if (remainingMs <= 0) return when (lang) {
            AppLanguage.RU -> "скоро"
            AppLanguage.PL -> "wkrótce"
            AppLanguage.EN -> "soon"
            AppLanguage.UA -> "незабаром"
        }
        val totalMinutes = remainingMs / (1000 * 60)
        val totalHours = totalMinutes / 60
        val days = totalHours / 24
        val hours = totalHours % 24
        val minutes = totalMinutes % 60

        return when (lang) {
            AppLanguage.RU -> {
                if (days > 0) "${days} дн ${hours} ч"
                else if (hours > 0) "${hours} ч ${minutes} мин"
                else "${minutes} мин"
            }
            AppLanguage.PL -> {
                if (days > 0) "${days} dni ${hours} godz"
                else if (hours > 0) "${hours} godz ${minutes} min"
                else "${minutes} min"
            }
            AppLanguage.EN -> {
                if (days > 0) "${days}d ${hours}h"
                else if (hours > 0) "${hours}h ${minutes}m"
                else "${minutes}m"
            }
            AppLanguage.UA -> {
                if (days > 0) "${days} дн ${hours} год"
                else if (hours > 0) "${hours} год ${minutes} хв"
                else "${minutes} хв"
            }
        }
    }

    fun freeScansCount(lang: AppLanguage, count: Int, resetRemainingMs: Long = 0L) = when (lang) {
        AppLanguage.RU -> {
            if (count >= 3 && resetRemainingMs > 0) {
                "3/3 сканов (сброс через ${formatTimeRemaining(lang, resetRemainingMs)})"
            } else if (count >= 3) {
                "3/3 сканов (лимит исчерпан)"
            } else {
                "$count/3 бесплатных сканов"
            }
        }
        AppLanguage.PL -> {
            if (count >= 3 && resetRemainingMs > 0) {
                "3/3 skanów (reset za ${formatTimeRemaining(lang, resetRemainingMs)})"
            } else if (count >= 3) {
                "3/3 skanów (limit osiągnięty)"
            } else {
                "$count/3 darmowych skanów"
            }
        }
        AppLanguage.EN -> {
            if (count >= 3 && resetRemainingMs > 0) {
                "3/3 scans (resets in ${formatTimeRemaining(lang, resetRemainingMs)})"
            } else if (count >= 3) {
                "3/3 scans (limit reached)"
            } else {
                "$count/3 free scans"
            }
        }
        AppLanguage.UA -> {
            if (count >= 3 && resetRemainingMs > 0) {
                "3/3 сканувань (скидання через ${formatTimeRemaining(lang, resetRemainingMs)})"
            } else if (count >= 3) {
                "3/3 сканувань (ліміт вичерпано)"
            } else {
                "$count/3 безкоштовних сканувань"
            }
        }
    }

    fun startDiagnosis(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Начать диагностику"
        AppLanguage.EN -> "Start AI Scan"
        AppLanguage.PL -> "Rozpocznij diagnostykę"
        AppLanguage.UA -> "Розпочати діагностику"
    }

    fun noObdRequired(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "OBD-адаптер не требуется"
        AppLanguage.EN -> "No OBD dongle required"
        AppLanguage.PL -> "Interfejs OBD niewymagany"
        AppLanguage.UA -> "OBD-адаптер не потрібен"
    }

    fun heroCardDescription(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Мгновенное распознавание горящих значков панели, посторонних шумов и кодов ошибок с помощью Gemini 1.5 Flash AI."
        AppLanguage.EN -> "Instant analysis of dashboard warning lights, engine sounds, and fault codes powered by Gemini 1.5 Flash AI."
        AppLanguage.PL -> "Błyskawiczna analiza kontrolek deski rozdzielczej, dźwięków silnika i kodów błędów za pomocą Gemini 1.5 Flash AI."
        AppLanguage.UA -> "Миттєве розпізнавання палаючих значків панелі, сторонніх шумів та кодів помилок за допомогою Gemini 1.5 Flash AI."
    }

    fun scheduledMaintenance(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Регламентные работы ТО"
        AppLanguage.EN -> "Scheduled Maintenance"
        AppLanguage.PL -> "Planowe czynności serwisowe"
        AppLanguage.UA -> "Регламентні роботи ТО"
    }

    fun tasksDue(lang: AppLanguage, count: Int) = when (lang) {
        AppLanguage.RU -> "$count работ требует внимания"
        AppLanguage.EN -> "$count tasks due soon"
        AppLanguage.PL -> "$count zadań wymaga uwagi"
        AppLanguage.UA -> "$count робіт вимагають уваги"
    }

    fun allServiceUpToDate(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Все работы выполнены! Автомобиль в порядке."
        AppLanguage.EN -> "All service tasks up to date!"
        AppLanguage.PL -> "Wszystkie przeglądy aktualne! Samochód w porządku."
        AppLanguage.UA -> "Усі роботи виконано! Автомобіль у порядку."
    }

    fun recommendedAtMileage(lang: AppLanguage, mileage: Int) = when (lang) {
        AppLanguage.RU -> "Рекомендовано при %,d км".format(mileage)
        AppLanguage.EN -> "Recommended at %,d km".format(mileage)
        AppLanguage.PL -> "Zalecane przy %,d km".format(mileage)
        AppLanguage.UA -> "Рекомендовано при %,d км".format(mileage)
    }

    fun view(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Обзор"
        AppLanguage.EN -> "View"
        AppLanguage.PL -> "Zobacz"
        AppLanguage.UA -> "Огляд"
    }

    fun estRepairCost(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Примерная стоимость ремонта"
        AppLanguage.EN -> "Est. Repair Cost"
        AppLanguage.PL -> "Szacowany koszt naprawy"
        AppLanguage.UA -> "Орієнтовна вартість ремонту"
    }

    fun estRepairCost(lang: AppLanguage, cost: String) = when (lang) {
        AppLanguage.RU -> "Ориентировочная стоимость: $cost"
        AppLanguage.EN -> "Est. Repair Cost: $cost"
        AppLanguage.PL -> "Szacowany koszt: $cost"
        AppLanguage.UA -> "Орієнтовна вартість: $cost"
    }

    fun viewReport(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Открыть отчёт"
        AppLanguage.EN -> "View Report"
        AppLanguage.PL -> "Otwórz raport"
        AppLanguage.UA -> "Відкрити звіт"
    }

    fun noDiagnosesYet(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Диагностик пока не проводилось"
        AppLanguage.EN -> "No Diagnostic History Yet"
        AppLanguage.PL -> "Brak historii diagnostyk"
        AppLanguage.UA -> "Діагностика ще не проводилася"
    }

    fun noDiagnosesSubtext(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Проведите первое сканирование при появлении шумов или ошибок"
        AppLanguage.EN -> "Run your first scan when warning lights or symptoms occur"
        AppLanguage.PL -> "Wykonaj pierwszy skan, gdy pojawią się kontrolki lub niepokojące objawy"
        AppLanguage.UA -> "Проведіть перше сканування при появі шумів або помилок"
    }

    fun startFirstDiagnosis(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Запустить первую диагностику"
        AppLanguage.EN -> "Run First Diagnostic Scan"
        AppLanguage.PL -> "Uruchom pierwszą diagnostykę"
        AppLanguage.UA -> "Запустити першу діагностику"
    }

    // DiagnosisInputScreen
    fun aiCarScannerTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "ИИ Автосканер"
        AppLanguage.EN -> "AI Car Scanner"
        AppLanguage.PL -> "Skaner AI"
        AppLanguage.UA -> "ШІ Автосканер"
    }

    fun aiCarScannerSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Мультимодальный анализ Gemini AI"
        AppLanguage.EN -> "Multimodal Gemini AI analysis"
        AppLanguage.PL -> "Multimodalna analiza Gemini AI"
        AppLanguage.UA -> "Мультимодальний аналіз Gemini AI"
    }

    fun vehicleInspected(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Проверяемый автомобиль:"
        AppLanguage.EN -> "Vehicle under inspection:"
        AppLanguage.PL -> "Badany pojazd:"
        AppLanguage.UA -> "Автомобіль, що перевіряється:"
    }

    fun tabDashboard(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Значок панели"
        AppLanguage.EN -> "Dashboard Light"
        AppLanguage.PL -> "Kontrolka"
        AppLanguage.UA -> "Значок панелі"
    }

    fun tabVoice(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Голос / Звук"
        AppLanguage.EN -> "Voice / Sound"
        AppLanguage.PL -> "Głos / Dźwięk"
        AppLanguage.UA -> "Голос / Звук"
    }

    fun tabText(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Текст / Ошибка"
        AppLanguage.EN -> "Text / DTC"
        AppLanguage.PL -> "Tekst / Kod"
        AppLanguage.UA -> "Текст / Помилка"
    }

    fun uploadPhotoPrompt(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Загрузите или сфотографируйте"
        AppLanguage.EN -> "Capture or Upload Photo"
        AppLanguage.PL -> "Zrób zdjęcie lub wybierz z galerii"
        AppLanguage.UA -> "Завантажте або сфотографуйте"
    }

    fun uploadPhotoSubtext(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Горящий значок Check Engine, масленка, ABS или поврежденный узел"
        AppLanguage.EN -> "Dashboard warning icon, Check Engine light, or damaged part"
        AppLanguage.PL -> "Kontrolka Check Engine, oleju, ABS lub uszkodzona część"
        AppLanguage.UA -> "Палаючий значок Check Engine, маслянка, ABS або пошкоджений вузол"
    }

    fun takePhotoButton(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Камера"
        AppLanguage.EN -> "Take Photo"
        AppLanguage.PL -> "Aparat"
        AppLanguage.UA -> "Камера"
    }

    fun galleryButton(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Галерея"
        AppLanguage.EN -> "Gallery"
        AppLanguage.PL -> "Galeria"
        AppLanguage.UA -> "Галерея"
    }

    fun listeningVoice(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Слушаю..."
        AppLanguage.EN -> "Listening..."
        AppLanguage.PL -> "Słucham..."
        AppLanguage.UA -> "Слухаю..."
    }

    fun speakClearly(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Опишите симптомы или странный шум четким голосом"
        AppLanguage.EN -> "Describe car symptoms or strange noise clearly"
        AppLanguage.PL -> "Opisz objawy lub dziwny dźwięk wyraźnym głosem"
        AppLanguage.UA -> "Опишіть симптоми або дивний шум чітким голосом"
    }

    fun simulateVoiceInput(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Имитировать запуск голоса"
        AppLanguage.EN -> "Simulate Voice Sample"
        AppLanguage.PL -> "Symuluj próbkę głosu"
        AppLanguage.UA -> "Імітувати запуск голосу"
    }

    fun tapToSpeak(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Нажмите микрофон для записи"
        AppLanguage.EN -> "Tap microphone to record"
        AppLanguage.PL -> "Dotknij mikrofonu, aby nagrać"
        AppLanguage.UA -> "Натисніть мікрофон для запису"
    }

    fun speakExample(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Пример: 'Свист при нажатии на тормоз на скорости 60 км/ч'"
        AppLanguage.EN -> "Example: 'Squeaking sound when pressing brake pedal'"
        AppLanguage.PL -> "Przykład: 'Piszczenie przy hamowaniu przy 60 km/h'"
        AppLanguage.UA -> "Приклад: 'Свист при натисканні на гальма на швидкості 60 км/год'"
    }

    fun problemDescriptionLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Описание проблемы или кода ошибки"
        AppLanguage.EN -> "Problem Description or DTC Code"
        AppLanguage.PL -> "Opis problemu lub kod błędu"
        AppLanguage.UA -> "Опис проблеми або коду помилки"
    }

    fun describePlaceholder(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Опишите симптомы (запах, звук, вибрация) или введите коды ошибок (P0420, P0300)..."
        AppLanguage.EN -> "Describe symptoms (smell, noise, vibration) or enter fault codes (P0420, P0300)..."
        AppLanguage.PL -> "Opisz objawy (zapach, dźwięk, wibracje) lub wprowadź kody błędów (P0420, P0300)..."
        AppLanguage.UA -> "Опишіть симптоми (запах, звук, вібрація) або введіть коди помилок (P0420, P0300)..."
    }

    fun quickSelectSymptoms(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Частые симптомы для быстрого выбора:"
        AppLanguage.EN -> "Quick Select Common Symptoms:"
        AppLanguage.PL -> "Częste objawy do szybkiego wyboru:"
        AppLanguage.UA -> "Часті симптоми для швидкого вибору:"
    }

    fun runAiDiagnosticScan(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Запустить ИИ Диагностику"
        AppLanguage.EN -> "Run AI Diagnostic Scan"
        AppLanguage.PL -> "Uruchom diagnostykę AI"
        AppLanguage.UA -> "Запустити ШІ Діагностику"
    }

    // ScanningScreen
    fun analyzingTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Нейросетевой анализ ИИ..."
        AppLanguage.EN -> "AI Neural Analysis..."
        AppLanguage.PL -> "Analiza sieci neuronowej AI..."
        AppLanguage.UA -> "Нейромережевий аналіз ШІ..."
    }

    fun scanInProgress(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Идет сопоставление симптомов с инженерной базой данных"
        AppLanguage.EN -> "Cross-referencing symptoms with mechanical database"
        AppLanguage.PL -> "Porównywanie objawów z bazą danych mechanicznych"
        AppLanguage.UA -> "Триває зіставлення симптомів з інженерною базою даних"
    }

    // DiagnosisResultScreen
    fun reportTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Отчёт ИИ Диагностики"
        AppLanguage.EN -> "AI Diagnostic Report"
        AppLanguage.PL -> "Raport diagnostyczny AI"
        AppLanguage.UA -> "Звіт ШІ Діагностики"
    }

    fun reportId(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Отчёт"
        AppLanguage.EN -> "Report"
        AppLanguage.PL -> "Raport"
        AppLanguage.UA -> "Звіт"
    }

    fun severityLevelLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "УРОВЕНЬ ОПАСНОСТИ ДВИЖЕНИЯ"
        AppLanguage.EN -> "SAFETY SEVERITY LEVEL"
        AppLanguage.PL -> "POZIOM BEZPIECZEŃSTWA JAZDY"
        AppLanguage.UA -> "РІВЕНЬ НЕБЕЗПЕКИ РУХУ"
    }

    fun plainLanguageTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Понятное объяснение автоэксперта"
        AppLanguage.EN -> "Plain Language Explanation"
        AppLanguage.PL -> "Zrozumiałe wyjaśnienie eksperta"
        AppLanguage.UA -> "Зрозуміле пояснення автоексперта"
    }

    fun technicalSummaryLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Техническое резюме:"
        AppLanguage.EN -> "Technical Summary:"
        AppLanguage.PL -> "Podsumowanie techniczne:"
        AppLanguage.UA -> "Технічне резюме:"
    }

    fun estRepairCostTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Оценка стоимости ремонта"
        AppLanguage.EN -> "Estimated Repair Cost"
        AppLanguage.PL -> "Szacowany koszt naprawy"
        AppLanguage.UA -> "Оцінка вартості ремонту"
    }

    fun repairCostIncludeNotice(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Включает примерные цены на оригинальные/аналоговые запчасти и работы в сертифицированных автосервисах."
        AppLanguage.EN -> "Includes estimated price for OEM/Aftermarket replacement parts + typical certified labor rate."
        AppLanguage.PL -> "Obejmuje szacowane ceny części zamiennych oraz koszt robocizny w serwisie."
        AppLanguage.UA -> "Включає орієнтовні ціни на оригінальні/аналогові запчастини та роботи в сертифікованих автосервісах."
    }

    fun diyGuideTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Инструкция по ремонту своими руками"
        AppLanguage.EN -> "DIY Repair Guide (Simple Fix)"
        AppLanguage.PL -> "Instrukcja samodzielnej naprawy (DIY)"
        AppLanguage.UA -> "Інструкція з ремонту своїми руками"
    }

    fun proMechanicTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Рекомендуется визит в автосервис"
        AppLanguage.EN -> "Professional Mechanic Recommended"
        AppLanguage.PL -> "Zalecana wizyta w warsztacie samochodowym"
        AppLanguage.UA -> "Рекомендується візит до автосервісу"
    }

    fun searchDiyVideos(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Найти видеоинструкцию на YouTube"
        AppLanguage.EN -> "Search DIY Video Instructions"
        AppLanguage.PL -> "Szukaj instrukcji wideo na YouTube"
        AppLanguage.UA -> "Знайти відеоінструкцію на YouTube"
    }

    fun shareReport(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Поделиться"
        AppLanguage.EN -> "Share Report"
        AppLanguage.PL -> "Udostępnij raport"
        AppLanguage.UA -> "Поділитися"
    }

    fun findMechanics(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Найти СТО рядом"
        AppLanguage.EN -> "Find Mechanics"
        AppLanguage.PL -> "Znajdź warsztat w pobliżu"
        AppLanguage.UA -> "Знайти СТО поруч"
    }

    fun findMechanicsProLocked(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "СТО рядом (PRO)"
        AppLanguage.EN -> "Find Mechanics (PRO)"
        AppLanguage.PL -> "Warsztat w pobliżu (PRO)"
        AppLanguage.UA -> "СТО поруч (PRO)"
    }

    fun freeReportBadge(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "БАЗОВЫЙ ОТЧЕТ (FREE)"
        AppLanguage.EN -> "BASIC REPORT (FREE)"
        AppLanguage.PL -> "RAPORT PODSTAWOWY (FREE)"
        AppLanguage.UA -> "БАЗОВИЙ ЗВІТ (FREE)"
    }

    fun proReportBadge(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "PRO ОТЧЕТ • ВСЁ ВКЛЮЧЕНО"
        AppLanguage.EN -> "PRO REPORT • FULL ACCESS"
        AppLanguage.PL -> "RAPORT PRO • PEŁNY DOSTĘP"
        AppLanguage.UA -> "PRO ЗВІТ • ПОВНИЙ ДОСТУП"
    }

    fun freeReportNotice(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "В бесплатной версии отображается только краткая сводка. Для полного анализа проблемы, пошаговой инструкции, видео на YouTube, поиска СТО и расчета стоимости перейдите на PRO."
        AppLanguage.EN -> "Only a brief summary is available in free version. Upgrade to PRO for in-depth AI analysis, DIY guides, YouTube tutorials, cost estimates, and nearby mechanics."
        AppLanguage.PL -> "W wersji bezpłatnej widoczne jest tylko krótkie podsumowanie. Przejdź na PRO, aby odblokować pełną analizę, instrukcje DIY, wideo na YouTube, koszty napraw i warsztaty."
        AppLanguage.UA -> "У безкоштовній версії відображається лише коротке резюме. Для повного аналізу проблеми, покрокової інструкції, відео на YouTube, пошуку СТО та розрахунку вартості перейдіть на PRO."
    }

    fun proLockedCostNote(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Оценка стоимости запчастей и нормо-часов СТО доступна только в тарифе PRO."
        AppLanguage.EN -> "Spare parts and labor cost calculation is exclusive to PRO members."
        AppLanguage.PL -> "Kalkulacja kosztów części i robocizny warsztatu jest dostępna tylko w PRO."
        AppLanguage.UA -> "Оцінка вартості запчастин та нормо-годин СТО доступна тільки в тарифі PRO."
    }

    fun proLockedDiyNote(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Пошаговая инструкция ремонта и поиск видеоуроков на YouTube доступны только в PRO версии."
        AppLanguage.EN -> "Step-by-step DIY repair guide and YouTube video instructions are available exclusively in PRO."
        AppLanguage.PL -> "Instrukcja naprawy krok po kroku i wyszukiwanie wideo na YouTube są dostępne tylko w wersji PRO."
        AppLanguage.UA -> "Покрокова інструкція ремонту та пошук відеоуроків на YouTube доступні тільки в PRO версії."
    }

    fun proLockedDetailNote(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Полный расширенный анализ причин, скрытых рисков и последствий доступен в PRO тарифе."
        AppLanguage.EN -> "Full in-depth analysis of root causes, hidden risks, and consequences is available in PRO."
        AppLanguage.PL -> "Pełna rozszerzona analiza przyczyn, ukrytych ryzyk i konsekwencji jest dostępna w planie PRO."
        AppLanguage.UA -> "Повний розширений аналіз причин, прихованих ризиків та наслідків доступний у PRO тарифі."
    }

    fun unlockProCta(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Разблокировать в PRO"
        AppLanguage.EN -> "Unlock in PRO"
        AppLanguage.PL -> "Odblokuj w PRO"
        AppLanguage.UA -> "Розблокувати в PRO"
    }

    fun costProOnly(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Доступно в PRO"
        AppLanguage.EN -> "Available in PRO"
        AppLanguage.PL -> "Dostępne w PRO"
        AppLanguage.UA -> "Доступно в PRO"
    }

    // CarProfileScreen (Garage)
    fun garageTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Гараж и Автомобили"
        AppLanguage.EN -> "Garage & Vehicles"
        AppLanguage.PL -> "Garaż i Pojazdy"
        AppLanguage.UA -> "Гараж та Автомобілі"
    }

    fun garageSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Управление машинами и графиком ТО"
        AppLanguage.EN -> "Manage cars & schedules"
        AppLanguage.PL -> "Zarządzanie autami i harmonogramem ТО"
        AppLanguage.UA -> "Управління машинами та графіком ТО"
    }

    fun addCarButton(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Добавить авто"
        AppLanguage.EN -> "Add Car"
        AppLanguage.PL -> "Dodaj auto"
        AppLanguage.UA -> "Додати авто"
    }

    fun primaryVehicleLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "ОСНОВНОЙ АВТОМОБИЛЬ"
        AppLanguage.EN -> "PRIMARY VEHICLE"
        AppLanguage.PL -> "GŁÓWNY POJAZD"
        AppLanguage.UA -> "ОСНОВНИЙ АВТОМОБІЛЬ"
    }

    fun currentOdometerLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Текущий пробег"
        AppLanguage.EN -> "Current Odometer"
        AppLanguage.PL -> "Aktualny przebieg"
        AppLanguage.UA -> "Поточний пробіг"
    }

    fun powertrainLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Двигатель"
        AppLanguage.EN -> "Powertrain Engine"
        AppLanguage.PL -> "Silnik"
        AppLanguage.UA -> "Двигун"
    }

    fun myVehiclesTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Мой гараж"
        AppLanguage.EN -> "My Vehicles"
        AppLanguage.PL -> "Mój garaż"
        AppLanguage.UA -> "Мій гараж"
    }

    fun activeLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Активный"
        AppLanguage.EN -> "Active"
        AppLanguage.PL -> "Aktywny"
        AppLanguage.UA -> "Активний"
    }

    fun scheduledChecklistTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Чек-лист обслуживания ТО"
        AppLanguage.EN -> "Scheduled Service Checklist"
        AppLanguage.PL -> "Lista czynności serwisowych"
        AppLanguage.UA -> "Чек-лист обслуговування ТО"
    }

    fun addTaskButtonLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "+ Добавить задачу"
        AppLanguage.EN -> "+ Add Task"
        AppLanguage.PL -> "+ Dodaj zadanie"
        AppLanguage.UA -> "+ Додати завдання"
    }

    fun proServiceNotice(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Чек-лист обслуживания и напоминания о замене расходников доступны только в PRO версии."
        AppLanguage.EN -> "Scheduled service checklist and maintenance alerts are available in PRO version."
        AppLanguage.PL -> "Lista czynności serwisowych i przypomnienia o wymianie są dostępne tylko w wersji PRO."
        AppLanguage.UA -> "Чек-лист обслуговування та нагадування про заміну витратників доступні тільки в PRO версії."
    }

    fun addTaskModalTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Добавить пункт в чек-лист"
        AppLanguage.EN -> "Add Checklist Task"
        AppLanguage.PL -> "Dodaj punkt do listy"
        AppLanguage.UA -> "Додати пункт до чек-листа"
    }

    fun taskTitleInputLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Название задачи (напр. Замена антифриза)"
        AppLanguage.EN -> "Task Title (e.g. Flush Coolant)"
        AppLanguage.PL -> "Tytuł zadania (np. Wymiana płynu chłodniczego)"
        AppLanguage.UA -> "Назва завдання (напр. Заміна антифризу)"
    }

    fun targetMileageInputLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Целевой пробег (км)"
        AppLanguage.EN -> "Target Mileage (km)"
        AppLanguage.PL -> "Docelowy przebieg (km)"
        AppLanguage.UA -> "Цільовий пробіг (км)"
    }

    fun translateTaskTitle(lang: AppLanguage, title: String): String {
        return when (lang) {
            AppLanguage.RU -> when {
                title.equals("Engine Oil & Filter Change", ignoreCase = true) -> "Замена масла в двигателе и фильтра"
                title.equals("Tire Rotation & Pressure Check", ignoreCase = true) -> "Перестановка колес и проверка давления"
                title.equals("Tire Rotation & Alignment Check", ignoreCase = true) -> "Перестановка колес и развал-схождение"
                title.equals("Front Brake Pad Inspection", ignoreCase = true) -> "Проверка передних тормозных колодок"
                title.equals("Brake Fluid & Pad Inspection", ignoreCase = true) -> "Проверка тормозной жидкости и колодок"
                title.equals("Cabin & Engine Air Filter Replacement", ignoreCase = true) -> "Замена салонного и воздушного фильтра"
                title.equals("Cabin Air Filter Replacement", ignoreCase = true) -> "Замена салонного фильтра"
                title.equals("Spark Plugs Replacement", ignoreCase = true) -> "Замена свечей зажигания"
                title.equals("Transmission Fluid Flush", ignoreCase = true) -> "Замена масла в КПП"
                title.equals("Coolant & Radiator Flush", ignoreCase = true) -> "Замена антифриза и промывка радиатора"
                title.equals("Battery & Electrical Test", ignoreCase = true) -> "Проверка аккумулятора и электрики"
                title.contains("Oil", ignoreCase = true) && title.contains("Filter", ignoreCase = true) -> "Замена масла и фильтра"
                title.contains("Tire", ignoreCase = true) -> "Перестановка колес и проверка шин"
                title.contains("Brake", ignoreCase = true) -> "Проверка тормозной системы"
                title.contains("Filter", ignoreCase = true) -> "Замена фильтров"
                title.contains("Spark Plug", ignoreCase = true) -> "Замена свечей зажигания"
                title.contains("Transmission", ignoreCase = true) -> "Замена масла в КПП"
                else -> title
            }
            AppLanguage.PL -> when {
                title.equals("Engine Oil & Filter Change", ignoreCase = true) || title == "Замена масла в двигателе и фильтра" || title == "Замена масла и фильтра" -> "Wymiana oleju silnikowego i filtra"
                title.equals("Tire Rotation & Pressure Check", ignoreCase = true) || title == "Перестановка колес и проверка давления" || title == "Перестановка колес и развал-схождение" || title == "Перестановка колес и проверка шин" -> "Rotacja kół i kontrola ciśnienia"
                title.equals("Front Brake Pad Inspection", ignoreCase = true) || title == "Проверка передних тормозных колодок" || title == "Проверка тормозной жидкости и колодок" || title == "Проверка тормозной системы" -> "Kontrola klocków hamulcowych"
                title.equals("Cabin & Engine Air Filter Replacement", ignoreCase = true) || title == "Замена салонного и воздушного фильтра" || title == "Замена салонного фильтра" || title == "Замена фильтров" -> "Wymiana filtra kabinowego i powietrza"
                title.equals("Spark Plugs Replacement", ignoreCase = true) || title == "Замена свечей зажигания" -> "Wymiana świec zapłonowych"
                title.equals("Transmission Fluid Flush", ignoreCase = true) || title == "Замена масла в КПП" -> "Wymiana oleju w skrzyni biegów"
                title.equals("Coolant & Radiator Flush", ignoreCase = true) || title == "Замена антифриза и промывка радиатора" -> "Wymiana płynu chłodniczego"
                title.equals("Battery & Electrical Test", ignoreCase = true) || title == "Test akumulatora i elektryki" -> "Test akumulatora i elektryki"
                else -> title
            }
            AppLanguage.UA -> when {
                title.equals("Engine Oil & Filter Change", ignoreCase = true) || title == "Замена масла в двигателе и фильтра" || title == "Замена масла и фильтра" || title == "Wymiana oleju silnikowego i filtra" -> "Заміна мастила в двигуні та фільтра"
                title.equals("Tire Rotation & Pressure Check", ignoreCase = true) || title == "Перестановка колес и проверка давления" || title == "Перестановка колес и развал-схождение" -> "Перестановка коліс та перевірка тиску"
                title.equals("Front Brake Pad Inspection", ignoreCase = true) || title == "Проверка передних тормозных колодок" || title == "Проверка тормозной жидкости и колодок" -> "Перевірка передніх гальмівних колодок"
                title.equals("Cabin & Engine Air Filter Replacement", ignoreCase = true) || title == "Замена салонного и воздушного фильтра" -> "Заміна салонного та повітряного фільтра"
                title.equals("Spark Plugs Replacement", ignoreCase = true) || title == "Замена свечей зажигания" -> "Заміна свічок запалювання"
                title.equals("Transmission Fluid Flush", ignoreCase = true) || title == "Замена масла в КПП" -> "Заміна мастила в КПП"
                title.equals("Coolant & Radiator Flush", ignoreCase = true) || title == "Замена антифриза и промывка радиатора" -> "Заміна антифризу та промивання радіатора"
                title.equals("Battery & Electrical Test", ignoreCase = true) || title == "Проверка аккумулятора и электрики" -> "Перевірка акумулятора та електрики"
                title.contains("Oil", ignoreCase = true) && title.contains("Filter", ignoreCase = true) -> "Заміна мастила та фільтра"
                title.contains("Tire", ignoreCase = true) -> "Перестановка коліс та перевірка шин"
                title.contains("Brake", ignoreCase = true) -> "Перевірка гальмівної системи"
                title.contains("Filter", ignoreCase = true) -> "Заміна фільтрів"
                title.contains("Spark Plug", ignoreCase = true) -> "Заміна свічок запалювання"
                title.contains("Transmission", ignoreCase = true) -> "Заміна мастила в КПП"
                else -> title
            }
            AppLanguage.EN -> when {
                title == "Замена масла в двигателе и фильтра" || title == "Замена масла и фильтра" || title == "Wymiana oleju silnikowego i filtra" || title == "Заміна мастила в двигуні та фільтра" -> "Engine Oil & Filter Change"
                title == "Перестановка колес и проверка давления" || title == "Перестановка колес и развал-схождение" || title == "Rotacja kół i kontrola ciśnienia" || title == "Перестановка коліс та перевірка тиску" -> "Tire Rotation & Pressure Check"
                title == "Проверка передних тормозных колодок" || title == "Kontrola klocków hamulcowych" || title == "Перевірка передніх гальмівних колодок" -> "Front Brake Pad Inspection"
                title == "Замена салонного и воздушного фильтра" || title == "Wymiana filtra kabinowego i powietrza" || title == "Заміна салонного та повітряного фільтра" -> "Cabin & Engine Air Filter Replacement"
                title == "Замена свечей зажигания" || title == "Wymiana świec zapłonowych" || title == "Заміна свічок запалювання" -> "Spark Plugs Replacement"
                title == "Замена масла в КПП" || title == "Wymiana oleju w skrzyni biegów" || title == "Заміна мастила в КПП" -> "Transmission Fluid Flush"
                title == "Замена антифриза и промывка радиатора" || title == "Wymiana płynu chłodniczego" || title == "Заміна антифризу та промивання радіатора" -> "Coolant & Radiator Flush"
                title == "Проверка аккумулятора и электрики" || title == "Test akumulatora i elektryki" || title == "Перевірка акумулятора та електрики" -> "Battery & Electrical Test"
                else -> title
            }
        }
    }

    fun targetMileage(lang: AppLanguage, mileage: Int) = when (lang) {
        AppLanguage.RU -> "План: %,d км".format(mileage)
        AppLanguage.EN -> "Target: %,d km".format(mileage)
        AppLanguage.PL -> "Plan: %,d km".format(mileage)
        AppLanguage.UA -> "План: %,d км".format(mileage)
    }

    fun statusDone(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Готово 🟢"
        AppLanguage.EN -> "Done 🟢"
        AppLanguage.PL -> "Gotowe 🟢"
        AppLanguage.UA -> "Готово 🟢"
    }

    fun statusDueNow(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Срочно 🔴"
        AppLanguage.EN -> "Due Now 🔴"
        AppLanguage.PL -> "Pilne 🔴"
        AppLanguage.UA -> "Терміново 🔴"
    }

    fun statusInKm(lang: AppLanguage, km: Int) = when (lang) {
        AppLanguage.RU -> "Через %,d км".format(km)
        AppLanguage.EN -> "In %,d km".format(km)
        AppLanguage.PL -> "Za %,d km".format(km)
        AppLanguage.UA -> "Через %,d км".format(km)
    }

    fun addVehicleModalTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Добавить новый автомобиль"
        AppLanguage.EN -> "Add New Vehicle"
        AppLanguage.PL -> "Dodaj nowy pojazd"
        AppLanguage.UA -> "Додати новий автомобіль"
    }

    fun addVehicleConfirm(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Добавить"
        AppLanguage.EN -> "Add Vehicle"
        AppLanguage.PL -> "Dodaj"
        AppLanguage.UA -> "Додати"
    }

    fun cancelButton(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Отмена"
        AppLanguage.EN -> "Cancel"
        AppLanguage.PL -> "Anuluj"
        AppLanguage.UA -> "Скасувати"
    }

    fun updateOdometerTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Обновить показания одометра"
        AppLanguage.EN -> "Update Odometer Mileage"
        AppLanguage.PL -> "Zaktualizuj stan licznika"
        AppLanguage.UA -> "Оновити показники одометра"
    }

    fun updateOdometerDescription(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Введите актуальный пробег автомобиля в километрах:"
        AppLanguage.EN -> "Enter updated vehicle odometer reading in kilometers:"
        AppLanguage.PL -> "Wprowadź aktualny przebieg samochodu w kilometrach:"
        AppLanguage.UA -> "Введіть актуальний пробіг автомобіля в кілометрах:"
    }

    fun updateButton(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Сохранить"
        AppLanguage.EN -> "Update"
        AppLanguage.PL -> "Zapisz"
        AppLanguage.UA -> "Зберегти"
    }

    // HistoryScreen
    fun historyTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "История проверок авто"
        AppLanguage.EN -> "Car Health History"
        AppLanguage.PL -> "Historia diagnostyki auta"
        AppLanguage.UA -> "Історія перевірок авто"
    }

    fun historySubtitle(lang: AppLanguage, count: Int) = when (lang) {
        AppLanguage.RU -> "Сохранено диагностических сессий: $count"
        AppLanguage.EN -> "$count past diagnostic sessions recorded"
        AppLanguage.PL -> "Zapisane sesje diagnostyczne: $count"
        AppLanguage.UA -> "Збережено діагностичних сесій: $count"
    }

    fun searchPlaceholder(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Поиск по ошибкам (P0420...), симптомам или кодам..."
        AppLanguage.EN -> "Search issues, codes (P0420...), or symptoms..."
        AppLanguage.PL -> "Szukaj po błędach (P0420...), objawach lub kodach..."
        AppLanguage.UA -> "Пошук по помилках (P0420...), симптомах або кодах..."
    }

    fun noHistoryFound(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Записи диагностик не найдены"
        AppLanguage.EN -> "No diagnostic history found"
        AppLanguage.PL -> "Nie znaleziono historii diagnostyki"
        AppLanguage.UA -> "Записи діагностик не знайдені"
    }

    fun noHistorySubtext(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Попробуйте сбросить фильтры или запустите новое сканирование."
        AppLanguage.EN -> "Try clearing search filters or run a new AI scan."
        AppLanguage.PL -> "Spróbuj zresetować filtry lub uruchom nowe skanowanie."
        AppLanguage.UA -> "Спробуйте скинути фільтри або запустіть нове сканування."
    }

    // PaywallScreen
    fun limitReached(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Исчерпан лимит бесплатных проверок (3/неделю)"
        AppLanguage.EN -> "Free diagnostic limit reached (3/week)"
        AppLanguage.PL -> "Wykorzystano limit darmowych skanów (3/tydzień)"
        AppLanguage.UA -> "Вичерпано ліміт безкоштовних перевірок (3/тиждень)"
    }

    fun limitResetCountdown(lang: AppLanguage, formattedTime: String) = when (lang) {
        AppLanguage.RU -> "Сброс через $formattedTime"
        AppLanguage.EN -> "Resets in $formattedTime"
        AppLanguage.PL -> "Reset za $formattedTime"
        AppLanguage.UA -> "Скидання через $formattedTime"
    }

    fun unlockPower(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Разблокируйте полный доступ к ИИ-механику"
        AppLanguage.EN -> "Unlock full mechanical power"
        AppLanguage.PL -> "Odblokuj pełny dostęp do mechanika AI"
        AppLanguage.UA -> "Розблокуйте повний доступ до ШІ-механіка"
    }

    fun deactivatePro(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "ОТКЛЮЧИТЬ PRO ДЕМО"
        AppLanguage.EN -> "DEACTIVATE PRO DEMO"
        AppLanguage.PL -> "WYŁĄCZ DEMO PRO"
        AppLanguage.UA -> "ВИМКНУТИ PRO ДЕМО"
    }

    fun activatePro(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "ПОДКЛЮЧИТЬ PRO ДОСТУП"
        AppLanguage.EN -> "UNLOCK PRO ACCESS NOW"
        AppLanguage.PL -> "ODBLOKUJ DOSTĘP PRO"
        AppLanguage.UA -> "ПІДКЛЮЧИТИ PRO ДОСТУП"
    }

    fun continueFreeTier(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Продолжить бесплатно (3 скана в неделю)"
        AppLanguage.EN -> "Continue with Free Tier (3 scans/week)"
        AppLanguage.PL -> "Kontynuuj za darmo (3 skany na tydzień)"
        AppLanguage.UA -> "Продовжити безкоштовно (3 сканування на тиждень)"
    }

    fun translateTechnicalSummary(lang: AppLanguage, original: String): String {
        if (lang == AppLanguage.EN) return original
        if (lang == AppLanguage.PL) {
            return when {
                original.contains("Catalytic Converter", ignoreCase = true) || original.contains("P0420", ignoreCase = true) ->
                    "Wydajność katalizatora poniżej progu (P0420)"
                original.contains("Brake Pad", ignoreCase = true) || original.contains("Brake Rotor", ignoreCase = true) ->
                    "Zużycie klocków hamulcowych / Bicie tarczy hamulcowej"
                original.contains("Low Engine Oil", ignoreCase = true) ->
                    "Niskie ciśnienie oleju silnikowego / Ostrzeżenie czujnika"
                original.contains("Low Battery", ignoreCase = true) || original.contains("Alternator", ignoreCase = true) ->
                    "Niskie napięcie akumulatora / Awaria alternatora"
                original.contains("Check Engine", ignoreCase = true) ->
                    "Kontrolka Check Engine / Błąd systemu (P0420 / P0171)"
                else -> original
            }
        }
        if (lang == AppLanguage.UA) {
            return when {
                original.contains("Catalytic Converter", ignoreCase = true) || original.contains("P0420", ignoreCase = true) ->
                    "Ефективність каталітичного нейтралізатора нижче порогу (P0420)"
                original.contains("Brake Pad", ignoreCase = true) || original.contains("Brake Rotor", ignoreCase = true) ->
                    "Знос гальмівних колодок / Биття гальмівного диска"
                original.contains("Low Engine Oil", ignoreCase = true) ->
                    "Низький тиск моторного мастила / Попередження датчика"
                original.contains("Low Battery", ignoreCase = true) || original.contains("Alternator", ignoreCase = true) ->
                    "Низька напруга АКБ / Несправність генератора"
                original.contains("Check Engine", ignoreCase = true) ->
                    "Індикатор Check Engine / Помилка системи (P0420 / P0171)"
                else -> original
            }
        }
        return when {
            original.contains("Catalytic Converter", ignoreCase = true) || original.contains("P0420", ignoreCase = true) ->
                "Эффективность каталитического нейтрализатора ниже порога (P0420)"
            original.contains("Brake Pad", ignoreCase = true) || original.contains("Brake Rotor", ignoreCase = true) ->
                "Износ тормозных колодок / Биение тормозного диска"
            original.contains("Low Engine Oil", ignoreCase = true) ->
                "Низкое давление моторного масла / Предупреждение датчика"
            original.contains("Low Battery", ignoreCase = true) || original.contains("Alternator", ignoreCase = true) ->
                "Низкое напряжение АКБ / Неисправность генератора"
            original.contains("Check Engine", ignoreCase = true) ->
                "Индикатор Check Engine / Ошибка системы (P0420 / P0171)"
            else -> original
        }
    }

    fun translatePlainExplanation(lang: AppLanguage, original: String): String {
        if (lang == AppLanguage.EN) return original
        if (lang == AppLanguage.PL) {
            return when {
                original.contains("catalytic converter", ignoreCase = true) ->
                    "Komputer pokładowy wykrył, że katalizator oczyszcza spaliny mniej efektywnie niż wymagano. Zwykle wiąże się to ze zużyciem katalizatora lub sondy lambda."
                original.contains("brake pads", ignoreCase = true) || original.contains("brake", ignoreCase = true) ->
                    "Przednie klocki hamulcowe posiadają wbudowane czujniki zużycia, wydające piszczący dźwięk, gdy grubość warstwy ściernej spadnie poniżej 3 mm."
                original.contains("oil pressure", ignoreCase = true) || original.contains("oil level", ignoreCase = true) ->
                    "Ciśnienie oleju silnikowego jest poniżej normy lub poziom spadł poniżej MIN. Ma to na celu ochronę silnika przed zatarciem."
                original.contains("battery", ignoreCase = true) || original.contains("starter", ignoreCase = true) ->
                    "Akumulator 12V traci pojemność lub alternator nie ładuje go dostatecznie podczas jazdy."
                else -> original
            }
        }
        if (lang == AppLanguage.UA) {
            return when {
                original.contains("catalytic converter", ignoreCase = true) ->
                    "Бортовий комп'ютер зафіксував, що каталітичний нейтралізатор очищає гази менш ефективно, ніж потрібно. Зазвичай це пов'язано зі зносом нейтралізатора або кисневого датчика."
                original.contains("brake pads", ignoreCase = true) || original.contains("brake", ignoreCase = true) ->
                    "Передні гальмівні колодки мають вбудовані датчики зносу, які видають характерний писк при зменшенні товщини фрикційного шару нижче 3 мм."
                original.contains("oil pressure", ignoreCase = true) || original.contains("oil level", ignoreCase = true) ->
                    "Тиск моторного мастила нижчий за норму або рівень впав нижче критичної позначки MIN. Це захищає деталі від масляного голодування."
                original.contains("battery", ignoreCase = true) || original.contains("starter", ignoreCase = true) ->
                    "Акумуляторна батарея 12V втрачає ємність або генератор заряджає її недостатньо ефективно під час руху."
                else -> original
            }
        }
        return when {
            original.contains("catalytic converter", ignoreCase = true) ->
                "Бортовой компьютер зафиксировал, что каталитический нейтрализатор очищает газы менее эффективно, чем требуется. Обычно это связано с износом нейтрализатора или кислородного датчика."
            original.contains("brake pads", ignoreCase = true) || original.contains("brake", ignoreCase = true) ->
                "Передние тормозные колодки имеют встроенные датчики износа, издающие характерный писк при уменьшении толщины фрикционного слоя ниже 3 мм."
            original.contains("oil pressure", ignoreCase = true) || original.contains("oil level", ignoreCase = true) ->
                "Давление моторного масла ниже нормы либо уровень упал ниже критической отметки MIN. Это защищает детали от масляного голодания."
            original.contains("battery", ignoreCase = true) || original.contains("starter", ignoreCase = true) ->
                "Аккумуляторная батарея 12V теряет емкость или генератор заряжает ее недостаточно эффективно во время движения."
            else -> original
        }
    }

    fun translateSeverityTitle(lang: AppLanguage, original: String): String {
        if (lang == AppLanguage.EN) return original
        if (lang == AppLanguage.PL) {
            return when {
                original.contains("Caution", ignoreCase = true) || original.contains("check", ignoreCase = true) ->
                    "🟡 Uwaga. Zaplanuj kontrolę."
                original.contains("Safe", ignoreCase = true) ->
                    "🟢 Bezpiecznie. Zaplanuj przegląd."
                original.contains("Danger", ignoreCase = true) ->
                    "🔴 Niebezpieczeństwo! Natychmiast zatrzymaj pojazd."
                else -> original
            }
        }
        if (lang == AppLanguage.UA) {
            return when {
                original.contains("Caution", ignoreCase = true) || original.contains("check", ignoreCase = true) ->
                    "🟡 Увага. Заплануйте перевірку."
                original.contains("Safe", ignoreCase = true) ->
                    "🟢 Безпечно для їзди. Заплануйте ТО."
                original.contains("Danger", ignoreCase = true) ->
                    "🔴 Небезпечно! Зупиніть автомобіль негайно."
                else -> original
            }
        }
        return when {
            original.contains("Caution", ignoreCase = true) || original.contains("check", ignoreCase = true) ->
                "🟡 Внимание. Запланируйте проверку."
            original.contains("Safe", ignoreCase = true) ->
                "🟢 Безопасно для езды. Запланируйте ТО."
            original.contains("Danger", ignoreCase = true) ->
                "🔴 Опасно! Остановите автомобиль немедленно."
            else -> original
        }
    }

    fun translateCostRange(lang: AppLanguage, original: String): String {
        if (original.contains("₽")) {
            return original
                .replace("35 000 ₽ - 65 000 ₽", "$400 - $800")
                .replace("12 000 ₽ - 25 000 ₽", "$150 - $300")
                .replace("3 000 ₽ - 8 000 ₽", "$40 - $100")
                .replace("8 000 ₽ - 18 000 ₽", "$100 - $220")
                .replace("10 000 ₽ - 30 000 ₽", "$120 - $350")
                .replace("5 000 ₽ - 15 000 ₽", "$60 - $180")
                .replace("₽", "$")
        }
        return original
    }

    fun translateRecommendedAction(lang: AppLanguage, original: String): String {
        if (lang == AppLanguage.EN) return original
        if (lang == AppLanguage.PL) {
            return when {
                original.contains("emissions", ignoreCase = true) || original.contains("mechanic", ignoreCase = true) ->
                    "Zaplanuj diagnostykę układu wydechowego w serwisie."
                original.contains("brake", ignoreCase = true) ->
                    "Wymień przednie klocki hamulcowe przy najbliższej okazji."
                else -> original
            }
        }
        if (lang == AppLanguage.UA) {
            return when {
                original.contains("emissions", ignoreCase = true) || original.contains("mechanic", ignoreCase = true) ->
                    "Заплануйте діагностику вихлопної системи на СТО."
                original.contains("brake", ignoreCase = true) ->
                    "Замініть передні гальмівні колодки при найближчому обслуговуванні."
                else -> original
            }
        }
        return when {
            original.contains("emissions", ignoreCase = true) || original.contains("mechanic", ignoreCase = true) ->
                "Запланируйте диагностику выхлопной системы на СТО."
            original.contains("brake", ignoreCase = true) ->
                "Замените передние тормозные колодки при ближайшем обслуживании."
            else -> original
        }
    }

    fun translateDiyInstructions(lang: AppLanguage, original: String): String {
        if (lang == AppLanguage.EN) return original
        if (lang == AppLanguage.PL) {
            if (original.contains("oxygen sensor", ignoreCase = true)) {
                return "Sprawdź przewody i złącza sondy lambda.||Skontroluj układ wydechowy pod kątem nieszczelności przed katalizatorem.||Skonsultuj się z mechanikiem w celu przeskanowania układu emisji."
            }
            if (original.contains("wheel lug nuts", ignoreCase = true)) {
                return "Poluzuj śruby kół i podnieś samochód na podnośniku.||Odkręć śruby zacisku i wyjmij stare klocki hamulcowe.||Nałóż smar przeciwpiszczący na blaszki i zamontuj nowe klocki."
            }
            return original
        }
        if (lang == AppLanguage.UA) {
            if (original.contains("oxygen sensor", ignoreCase = true)) {
                return "Перевірте проводку та роз'єми датчика кисню.||Огляньте вихлопну систему на наявність витоків до каталізатора.||Зверніться до автомеханіка для сканування системи викидів."
            }
            if (original.contains("wheel lug nuts", ignoreCase = true)) {
                return "Ослабте колісні гайки та підніміть автомобіль на домкраті.||Зніміть болти супорта та витягніть старі гальмівні колодки.||Нанесіть протискрипну змазку на металеві пластини та встановіть нові колодки."
            }
            return original
        }
        if (original.contains("oxygen sensor", ignoreCase = true)) {
            return "Проверьте проводку и разъемы датчика кислорода.||Осмотрите выхлопную систему на наличие утечек до катализатора.||Обратитесь к автомеханику для сканирования системы выбросов."
        }
        if (original.contains("wheel lug nuts", ignoreCase = true)) {
            return "Ослабьте колесные гайки и поднимите автомобиль на домкрате.||Снимите болты суппорта и извлеките старые тормозные колодки.||Нанесите противоскрипную смазку на металлические пластины и установите новые колодки."
        }
        return original
    }

    fun formatMileage(lang: AppLanguage, mileage: Int): String {
        return when (lang) {
            AppLanguage.RU -> "%,d км".format(mileage)
            AppLanguage.PL -> "%,d km".format(mileage)
            AppLanguage.EN -> "%,d km".format(mileage)
            AppLanguage.UA -> "%,d км".format(mileage)
        }
    }

    fun translateEngineType(lang: AppLanguage, engine: String): String {
        if (lang == AppLanguage.EN) return engine
        if (lang == AppLanguage.PL) {
            return when {
                engine.contains("Gasoline", ignoreCase = true) -> engine.replace("Gasoline", "Benzyna", ignoreCase = true)
                engine.contains("Hybrid", ignoreCase = true) -> engine.replace("Hybrid", "Hybryda", ignoreCase = true)
                engine.contains("Electric", ignoreCase = true) || engine.contains("EV", ignoreCase = true) -> "Elektryczny"
                engine.contains("Diesel", ignoreCase = true) -> engine.replace("Diesel", "Diesel", ignoreCase = true)
                else -> engine
            }
        }
        if (lang == AppLanguage.UA) {
            return when {
                engine.contains("Gasoline", ignoreCase = true) -> engine.replace("Gasoline", "Бензин", ignoreCase = true)
                engine.contains("Hybrid", ignoreCase = true) -> engine.replace("Hybrid", "Гібрид", ignoreCase = true)
                engine.contains("Electric", ignoreCase = true) || engine.contains("EV", ignoreCase = true) -> "Електро"
                engine.contains("Diesel", ignoreCase = true) -> engine.replace("Diesel", "Дизель", ignoreCase = true)
                else -> engine
            }
        }
        return when {
            engine.contains("Gasoline", ignoreCase = true) -> engine.replace("Gasoline", "Бензин", ignoreCase = true)
            engine.contains("Hybrid", ignoreCase = true) -> engine.replace("Hybrid", "Гибрид", ignoreCase = true)
            engine.contains("Electric", ignoreCase = true) || engine.contains("EV", ignoreCase = true) -> "Электро"
            engine.contains("Diesel", ignoreCase = true) -> engine.replace("Diesel", "Дизель", ignoreCase = true)
            else -> engine
        }
    }

    fun deleteReport(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Удалить"
        AppLanguage.EN -> "Delete"
        AppLanguage.PL -> "Usuń"
        AppLanguage.UA -> "Видалити"
    }

    fun deleteReportConfirmTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Удалить запись сканирования?"
        AppLanguage.EN -> "Delete Scan Record?"
        AppLanguage.PL -> "Usunąć wpis diagnostyki?"
        AppLanguage.UA -> "Видалити запис сканування?"
    }

    fun deleteReportConfirmMessage(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Этот отчет диагностики будет безвозвратно удален из истории."
        AppLanguage.EN -> "This diagnostic report will be permanently removed from your history."
        AppLanguage.PL -> "Ten raport diagnostyczny zostanie trwale usunięty z historii."
        AppLanguage.UA -> "Цей звіт діагностики буде безповоротно видалено з історії."
    }

    fun clearAllHistory(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Очистить всё"
        AppLanguage.EN -> "Clear All"
        AppLanguage.PL -> "Wyczyść wszystko"
        AppLanguage.UA -> "Очистити все"
    }

    fun clearAllConfirmTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Очистить всю историю?"
        AppLanguage.EN -> "Clear All History?"
        AppLanguage.PL -> "Wyczyścić całą historię?"
        AppLanguage.UA -> "Очистити всю історію?"
    }

    fun clearAllConfirmMessage(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Все сохраненные записи сканирования будут удалены без возможности восстановления."
        AppLanguage.EN -> "All saved diagnostic records will be deleted permanently."
        AppLanguage.PL -> "Wszystkie zapisane wpisy diagnostyczne zostaną trwale usunięte."
        AppLanguage.UA -> "Усі збережені записи сканування будуть видалені без можливості відновлення."
    }

    fun freeHistoryNotice(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "В бесплатной версии история сканирований временная и автоматически исчезает после закрытия приложения. В тарифе PRO история сохраняется навсегда с возможностью ручного удаления записей."
        AppLanguage.EN -> "In the free version, scan history is temporary and clears automatically after closing the app. In PRO, history is stored permanently with full manual delete control."
        AppLanguage.PL -> "W wersji bezpłatnej historia skanowania jest tymczasowa i znika po zamknięciu aplikacji. W wersji PRO historia jest zachowywana na stałe z opcją usuwania."
        AppLanguage.UA -> "У безкоштовній версії історія сканувань тимчасова і автоматично зникає після закриття програми. У тарифі PRO історія зберігається назавжди з можливістю ручного видалення записів."
    }

    fun proManageHistoryBadge(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "PRO • Постоянная история"
        AppLanguage.EN -> "PRO • Permanent History"
        AppLanguage.PL -> "PRO • Trwała historia"
        AppLanguage.UA -> "PRO • Постійна історія"
    }
}

