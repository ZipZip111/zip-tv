package com.ultratv.tv.nativeapp

/** Zip-TV product flags — fork branding vs upstream Ultra TV. */
object ProductConfig {
    const val APP_DISPLAY_NAME = "Zip-TV"
    const val CREDIT_URL = "https://zip-dev.ru"

    /** Hide Cloudflare MAC sync — not needed for iptv-org users. */
    const val SHOW_CLOUD_SYNC = false

    /** On first launch, add iptv-org Russia + EPG automatically. */
    const val AUTO_BOOTSTRAP_IPTV_ORG = true

    const val DEFAULT_LANGUAGE = "ru"
    const val DEFAULT_IPTV_ORG_PRESET_ID = "ru"

    /** Extra iptv-org playlists added on first launch (live movie/series channels). */
    val BOOTSTRAP_EXTRA_PRESET_IDS = listOf("movies", "series")

    /** Disable outbound crash telemetry by default for Zip-TV installs. */
    const val DEFAULT_TELEMETRY = false
}
