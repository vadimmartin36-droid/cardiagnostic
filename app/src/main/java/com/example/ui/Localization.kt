package com.example.ui

enum class AppLanguage {
    RU, EN
}

object Localization {

    fun appTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "CarDiagnostic AI"
        AppLanguage.EN -> "CarDiagnostic AI"
    }

    fun appSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Умная автодиагностика"
        AppLanguage.EN -> "Smart Vehicle Diagnostics"
    }

    fun appVersion(lang: AppLanguage) = "v0.0.1"

    fun developerInfo(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Разработчик: Vadym Martynsku"
        AppLanguage.EN -> "Developer: Vadym Martynsku"
    }

    fun freeScansBadge(lang: AppLanguage, count: Int) = when (lang) {
        AppLanguage.RU -> "$count/3 сканов"
        AppLanguage.EN -> "$count/3 Free Scans"
    }

    fun proUnlimitedBadge(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "PRO БЕЗЛИМИТ"
        AppLanguage.EN -> "PRO UNLIMITED"
    }

    fun appSettingsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Настройки и Лимиты"
        AppLanguage.EN -> "Settings & Limits"
    }

    fun languageLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Язык интерфейса"
        AppLanguage.EN -> "Interface Language"
    }

    fun scansLimitLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Доступные сканирования"
        AppLanguage.EN -> "Available Scans"
    }

    // Nav Labels
    fun navHome(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Главная"
        AppLanguage.EN -> "Home"
    }

    fun navScan(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Сканер"
        AppLanguage.EN -> "Scan"
    }

    fun navHistory(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "История"
        AppLanguage.EN -> "History"
    }

    fun navGarage(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Гараж"
        AppLanguage.EN -> "Garage"
    }

    fun navPro(lang: AppLanguage, isPro: Boolean) = when (lang) {
        AppLanguage.RU -> "PRO"
        AppLanguage.EN -> if (isPro) "Pro" else "Get Pro"
    }

    // HomeScreen
    fun recentDiagnoses(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "История проверок"
        AppLanguage.EN -> "Recent Diagnoses"
    }

    fun viewAll(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Все записи"
        AppLanguage.EN -> "View All"
    }

    fun proUnlimited(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "PRO БЕЗЛИМИТ"
        AppLanguage.EN -> "PRO UNLIMITED"
    }

    fun freeScansCount(lang: AppLanguage, count: Int) = when (lang) {
        AppLanguage.RU -> "$count/3 бесплатных сканов"
        AppLanguage.EN -> "$count/3 free scans"
    }

    fun startDiagnosis(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Начать диагностику"
        AppLanguage.EN -> "Start AI Scan"
    }

    fun noObdRequired(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "OBD-адаптер не требуется"
        AppLanguage.EN -> "No OBD dongle required"
    }

    fun heroCardDescription(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Мгновенное распознавание горящих значков панели, посторонних шумов и кодов ошибок с помощью Gemini 1.5 Flash AI."
        AppLanguage.EN -> "Instant analysis of dashboard warning lights, engine sounds, and fault codes powered by Gemini 1.5 Flash AI."
    }

    fun scheduledMaintenance(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Регламентные работы ТО"
        AppLanguage.EN -> "Scheduled Maintenance"
    }

    fun tasksDue(lang: AppLanguage, count: Int) = when (lang) {
        AppLanguage.RU -> "$count работ требует внимания"
        AppLanguage.EN -> "$count tasks due soon"
    }

    fun allServiceUpToDate(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Все работы выполнены! Автомобиль в порядке."
        AppLanguage.EN -> "All service tasks up to date!"
    }

    fun recommendedAtMileage(lang: AppLanguage, mileage: Int) = when (lang) {
        AppLanguage.RU -> "Рекомендовано при %,d км".format(mileage)
        AppLanguage.EN -> "Recommended at %,d km".format(mileage)
    }

    fun view(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Обзор"
        AppLanguage.EN -> "View"
    }

    fun estRepairCost(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Примерная стоимость ремонта"
        AppLanguage.EN -> "Est. Repair Cost"
    }

    fun estRepairCost(lang: AppLanguage, cost: String) = when (lang) {
        AppLanguage.RU -> "Ориентировочная стоимость: $cost"
        AppLanguage.EN -> "Est. Repair Cost: $cost"
    }

    fun viewReport(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Открыть отчёт"
        AppLanguage.EN -> "View Report"
    }

    fun noDiagnosesYet(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Диагностик пока не проводилось"
        AppLanguage.EN -> "No Diagnostic History Yet"
    }

    fun noDiagnosesSubtext(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Проведите первое сканирование при появлении шумов или ошибок"
        AppLanguage.EN -> "Run your first scan when warning lights or symptoms occur"
    }

    fun startFirstDiagnosis(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Запустить первую диагностику"
        AppLanguage.EN -> "Run First Diagnostic Scan"
    }

    // DiagnosisInputScreen
    fun aiCarScannerTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "ИИ Автосканер"
        AppLanguage.EN -> "AI Car Scanner"
    }

    fun aiCarScannerSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Мультимодальный анализ Gemini AI"
        AppLanguage.EN -> "Multimodal Gemini AI analysis"
    }

    fun vehicleInspected(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Проверяемый автомобиль:"
        AppLanguage.EN -> "Vehicle under inspection:"
    }

    fun tabDashboard(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Значок панели"
        AppLanguage.EN -> "Dashboard Light"
    }

    fun tabVoice(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Голос / Звук"
        AppLanguage.EN -> "Voice / Sound"
    }

    fun tabText(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Текст / Ошибка"
        AppLanguage.EN -> "Text / DTC"
    }

    fun uploadPhotoPrompt(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Загрузите или сфотографируйте"
        AppLanguage.EN -> "Capture or Upload Photo"
    }

    fun uploadPhotoSubtext(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Горящий значок Check Engine, масленка, ABS или поврежденный узел"
        AppLanguage.EN -> "Dashboard warning icon, Check Engine light, or damaged part"
    }

    fun takePhotoButton(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Камера"
        AppLanguage.EN -> "Take Photo"
    }

    fun galleryButton(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Галерея"
        AppLanguage.EN -> "Gallery"
    }

    fun listeningVoice(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Слушаю..."
        AppLanguage.EN -> "Listening..."
    }

    fun speakClearly(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Опишите симптомы или странный шум четким голосом"
        AppLanguage.EN -> "Describe car symptoms or strange noise clearly"
    }

    fun simulateVoiceInput(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Имитировать запуск голоса"
        AppLanguage.EN -> "Simulate Voice Sample"
    }

    fun tapToSpeak(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Нажмите микрофон для записи"
        AppLanguage.EN -> "Tap microphone to record"
    }

    fun speakExample(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Пример: 'Свист при нажатии на тормоз на скорости 60 км/ч'"
        AppLanguage.EN -> "Example: 'Squeaking sound when pressing brake pedal'"
    }

    fun problemDescriptionLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Описание проблемы или кода ошибки"
        AppLanguage.EN -> "Problem Description or DTC Code"
    }

    fun describePlaceholder(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Опишите симптомы (запах, звук, вибрация) или введите коды ошибок (P0420, P0300)..."
        AppLanguage.EN -> "Describe symptoms (smell, noise, vibration) or enter fault codes (P0420, P0300)..."
    }

    fun quickSelectSymptoms(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Частые симптомы для быстрого выбора:"
        AppLanguage.EN -> "Quick Select Common Symptoms:"
    }

    fun runAiDiagnosticScan(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Запустить ИИ Диагностику"
        AppLanguage.EN -> "Run AI Diagnostic Scan"
    }

    // ScanningScreen
    fun analyzingTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Нейросетевой анализ ИИ..."
        AppLanguage.EN -> "AI Neural Analysis..."
    }

    fun scanInProgress(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Идет сопоставление симптомов с инженерной базой данных"
        AppLanguage.EN -> "Cross-referencing symptoms with mechanical database"
    }

    // DiagnosisResultScreen
    fun reportTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Отчёт ИИ Диагностики"
        AppLanguage.EN -> "AI Diagnostic Report"
    }

    fun reportId(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Отчёт"
        AppLanguage.EN -> "Report"
    }

    fun severityLevelLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "УРОВЕНЬ ОПАСНОСТИ ДВИЖЕНИЯ"
        AppLanguage.EN -> "SAFETY SEVERITY LEVEL"
    }

    fun plainLanguageTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Понятное объяснение автоэксперта"
        AppLanguage.EN -> "Plain Language Explanation"
    }

    fun technicalSummaryLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Техническое резюме:"
        AppLanguage.EN -> "Technical Summary:"
    }

    fun estRepairCostTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Оценка стоимости ремонта"
        AppLanguage.EN -> "Estimated Repair Cost"
    }

    fun repairCostIncludeNotice(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Включает примерные цены на оригинальные/аналоговые запчасти и работы в сертифицированных автосервисах."
        AppLanguage.EN -> "Includes estimated price for OEM/Aftermarket replacement parts + typical certified labor rate."
    }

    fun diyGuideTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Инструкция по ремонту своими руками"
        AppLanguage.EN -> "DIY Repair Guide (Simple Fix)"
    }

    fun proMechanicTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Рекомендуется визит в автосервис"
        AppLanguage.EN -> "Professional Mechanic Recommended"
    }

    fun searchDiyVideos(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Найти видеоинструкцию на YouTube"
        AppLanguage.EN -> "Search DIY Video Instructions"
    }

    fun shareReport(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Поделиться"
        AppLanguage.EN -> "Share Report"
    }

    fun findMechanics(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Найти СТО рядом"
        AppLanguage.EN -> "Find Mechanics"
    }

    // CarProfileScreen (Garage)
    fun garageTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Гараж и Автомобили"
        AppLanguage.EN -> "Garage & Vehicles"
    }

    fun garageSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Управление машинами и графиком ТО"
        AppLanguage.EN -> "Manage cars & schedules"
    }

    fun addCarButton(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Добавить авто"
        AppLanguage.EN -> "Add Car"
    }

    fun primaryVehicleLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "ОСНОВНОЙ АВТОМОБИЛЬ"
        AppLanguage.EN -> "PRIMARY VEHICLE"
    }

    fun currentOdometerLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Текущий пробег"
        AppLanguage.EN -> "Current Odometer"
    }

    fun powertrainLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Двигатель"
        AppLanguage.EN -> "Powertrain Engine"
    }

    fun myVehiclesTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Мой гараж"
        AppLanguage.EN -> "My Vehicles"
    }

    fun activeLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Активный"
        AppLanguage.EN -> "Active"
    }

    fun scheduledChecklistTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Чек-лист обслуживания ТО"
        AppLanguage.EN -> "Scheduled Service Checklist"
    }

    fun addTaskButtonLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "+ Добавить задачу"
        AppLanguage.EN -> "+ Add Task"
    }

    fun addTaskModalTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Добавить пункт в чек-лист"
        AppLanguage.EN -> "Add Checklist Task"
    }

    fun taskTitleInputLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Название задачи (напр. Замена антифриза)"
        AppLanguage.EN -> "Task Title (e.g. Flush Coolant)"
    }

    fun targetMileageInputLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Целевой пробег (км)"
        AppLanguage.EN -> "Target Mileage (km)"
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
            AppLanguage.EN -> when {
                title == "Замена масла в двигателе и фильтра" || title == "Замена масла и фильтра" -> "Engine Oil & Filter Change"
                title == "Перестановка колес и проверка давления" || title == "Перестановка колес и развал-схождение" || title == "Перестановка колес и проверка шин" -> "Tire Rotation & Pressure Check"
                title == "Проверка передних тормозных колодок" || title == "Проверка тормозной жидкости и колодок" || title == "Проверка тормозной системы" -> "Front Brake Pad Inspection"
                title == "Замена салонного и воздушного фильтра" || title == "Замена салонного фильтра" || title == "Замена фильтров" -> "Cabin & Engine Air Filter Replacement"
                title == "Замена свечей зажигания" -> "Spark Plugs Replacement"
                title == "Замена масла в КПП" -> "Transmission Fluid Flush"
                title == "Замена антифриза и промывка радиатора" -> "Coolant & Radiator Flush"
                title == "Проверка аккумулятора и электрики" -> "Battery & Electrical Test"
                else -> title
            }
        }
    }

    fun targetMileage(lang: AppLanguage, mileage: Int) = when (lang) {
        AppLanguage.RU -> "План: %,d км".format(mileage)
        AppLanguage.EN -> "Target: %,d km".format(mileage)
    }

    fun statusDone(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Готово 🟢"
        AppLanguage.EN -> "Done 🟢"
    }

    fun statusDueNow(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Срочно 🔴"
        AppLanguage.EN -> "Due Now 🔴"
    }

    fun statusInKm(lang: AppLanguage, km: Int) = when (lang) {
        AppLanguage.RU -> "Через %,d км".format(km)
        AppLanguage.EN -> "In %,d km".format(km)
    }

    fun addVehicleModalTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Добавить новый автомобиль"
        AppLanguage.EN -> "Add New Vehicle"
    }

    fun addVehicleConfirm(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Добавить"
        AppLanguage.EN -> "Add Vehicle"
    }

    fun cancelButton(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Отмена"
        AppLanguage.EN -> "Cancel"
    }

    fun updateOdometerTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Обновить показания одометра"
        AppLanguage.EN -> "Update Odometer Mileage"
    }

    fun updateOdometerDescription(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Введите актуальный пробег автомобиля в километрах:"
        AppLanguage.EN -> "Enter updated vehicle odometer reading in kilometers:"
    }

    fun updateButton(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Сохранить"
        AppLanguage.EN -> "Update"
    }

    // HistoryScreen
    fun historyTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "История проверок авто"
        AppLanguage.EN -> "Car Health History"
    }

    fun historySubtitle(lang: AppLanguage, count: Int) = when (lang) {
        AppLanguage.RU -> "Сохранено диагностических сессий: $count"
        AppLanguage.EN -> "$count past diagnostic sessions recorded"
    }

    fun searchPlaceholder(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Поиск по ошибкам (P0420...), симптомам или кодам..."
        AppLanguage.EN -> "Search issues, codes (P0420...), or symptoms..."
    }

    fun noHistoryFound(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Записи диагностик не найдены"
        AppLanguage.EN -> "No diagnostic history found"
    }

    fun noHistorySubtext(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Попробуйте сбросить фильтры или запустите новое сканирование."
        AppLanguage.EN -> "Try clearing search filters or run a new AI scan."
    }

    // PaywallScreen
    fun limitReached(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Исчерпан лимит бесплатных проверок (3/месяц)"
        AppLanguage.EN -> "Free diagnostic limit reached (3/month)"
    }

    fun unlockPower(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Разблокируйте полный доступ к ИИ-механику"
        AppLanguage.EN -> "Unlock full mechanical power"
    }

    fun deactivatePro(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "ОТКЛЮЧИТЬ PRO ДЕМО"
        AppLanguage.EN -> "DEACTIVATE PRO DEMO"
    }

    fun activatePro(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "ПОДКЛЮЧИТЬ PRO ДОСТУП"
        AppLanguage.EN -> "UNLOCK PRO ACCESS NOW"
    }

    fun continueFreeTier(lang: AppLanguage) = when (lang) {
        AppLanguage.RU -> "Продолжить бесплатно (3 скана в месяц)"
        AppLanguage.EN -> "Continue with Free Tier (3 scans/month)"
    }

    fun translateTechnicalSummary(lang: AppLanguage, original: String): String {
        if (lang != AppLanguage.RU) return original
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
        if (lang != AppLanguage.RU) return original
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
        if (lang != AppLanguage.RU) return original
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
        if (lang != AppLanguage.RU) return original
        return when {
            original.contains("emissions", ignoreCase = true) || original.contains("mechanic", ignoreCase = true) ->
                "Запланируйте диагностику выхлопной системы на СТО."
            original.contains("brake", ignoreCase = true) ->
                "Замените передние тормозные колодки при ближайшем обслуживании."
            else -> original
        }
    }

    fun translateDiyInstructions(lang: AppLanguage, original: String): String {
        if (lang != AppLanguage.RU) return original
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
            AppLanguage.EN -> "%,d km".format(mileage)
        }
    }

    fun translateEngineType(lang: AppLanguage, engine: String): String {
        if (lang != AppLanguage.RU) return engine
        return when {
            engine.contains("Gasoline", ignoreCase = true) -> engine.replace("Gasoline", "Бензин", ignoreCase = true)
            engine.contains("Hybrid", ignoreCase = true) -> engine.replace("Hybrid", "Гибрид", ignoreCase = true)
            engine.contains("Electric", ignoreCase = true) || engine.contains("EV", ignoreCase = true) -> "Электро"
            engine.contains("Diesel", ignoreCase = true) -> engine.replace("Diesel", "Дизель", ignoreCase = true)
            else -> engine
        }
    }
}
