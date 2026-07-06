package tv.own.owntv

/**
 * Zip-TV product layer on top of OwnTV. Playback/sync/player code stays untouched —
 * only branding, bootstrap playlists, and copy live here.
 */
object ProductConfig {
    const val APP_DISPLAY_NAME = "Zip-TV"
    const val CREDIT_URL = "https://zip-dev.ru"
    const val CREDIT_LABEL = "сайт разработан zip-dev.ru"
    const val GITHUB_REPO = "ZipZip111/zip-tv"

    /** First-run: import iptv-org presets automatically (no manual M3U step). */
    const val AUTO_BOOTSTRAP = true

    const val DEFAULT_PROFILE_NAME = "Гость"

    data class BootstrapPreset(
        val name: String,
        val playlistUrl: String,
        val epgUrl: String = "",
    )

    val BOOTSTRAP_PRESETS = listOf(
        BootstrapPreset(
            name = "🇷🇺 IPTV-org · Россия",
            playlistUrl = "https://iptv-org.github.io/iptv/countries/ru.m3u",
            epgUrl = "https://iptv-org.github.io/epg/guides/ru.xml",
        ),
        BootstrapPreset(
            name = "🎬 IPTV-org · Кино",
            playlistUrl = "https://iptv-org.github.io/iptv/categories/movies.m3u",
        ),
        BootstrapPreset(
            name = "📺 IPTV-org · Сериалы",
            playlistUrl = "https://iptv-org.github.io/iptv/categories/series.m3u",
        ),
    )
}
