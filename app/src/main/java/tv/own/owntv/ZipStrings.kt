package tv.own.owntv

import tv.own.owntv.features.shell.MainSection

/** Russian UI copy for Zip-TV (onboarding + shell + settings highlights). */
object ZipStrings {
    fun section(section: MainSection): String = when (section) {
        MainSection.SEARCH -> "Поиск"
        MainSection.HOME -> "Главная"
        MainSection.LIVE_TV -> "Эфир"
        MainSection.MOVIES -> "Фильмы"
        MainSection.SERIES -> "Сериалы"
        MainSection.DOWNLOADS -> "Загрузки"
        MainSection.EPG -> "Телегид"
        MainSection.SETTINGS -> "Настройки"
    }

    const val welcomeTagline = "IPTV-плеер для Android TV"
    const val getStarted = "Начать"
    const val disclaimerTitle = "Перед началом"
    const val disclaimerBody =
        "${ProductConfig.APP_DISPLAY_NAME} — только медиаплеер. Каналов и контента внутри нет. " +
            "Вы сами добавляете легальные M3U/Xtream источники. Автонастройка подключит плейлист iptv-org (Россия)."
    const val back = "Назад"
    const val understand = "Понятно"
    val setupTitle get() = "Настройка ${ProductConfig.APP_DISPLAY_NAME}"
    const val setupSubtitle = "Создайте профиль — плейлисты iptv-org добавятся автоматически."
    const val newProfile = "Новый профиль"
    val newProfileDesc get() = "Профиль «${ProductConfig.DEFAULT_PROFILE_NAME}» и каталог каналов"
    const val restoreBackup = "Восстановить backup"
    const val restoreBackupDesc = "Импорт профилей и плейлистов из файла"
    const val addPlaylist = "Добавить плейлист"
    const val addPlaylistHint = "Или настройте источники позже в Настройках."
    const val skipForNow = "Пропустить"
    const val settings = "Настройки"
    const val about = "О приложении"
    const val aboutDesc = "Версия, лицензия, zip-dev.ru"
    const val close = "Закрыть"
    val aboutBody get() =
        "${ProductConfig.APP_DISPLAY_NAME} — IPTV-плеер для Android TV на базе OwnTV (GPLv3). " +
            "Контент не предоставляется; источники добавляет пользователь."
    const val checkUpdates = "Проверить обновления"
    const val checkUpdatesDesc = "Скачать новую версию с GitHub"
    const val importDone = "Готово!"
    const val continueLabel = "Продолжить"
}
