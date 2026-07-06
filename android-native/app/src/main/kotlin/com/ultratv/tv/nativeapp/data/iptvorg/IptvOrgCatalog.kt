package com.ultratv.tv.nativeapp.data.iptvorg

/**
 * Curated [iptv-org/iptv](https://github.com/iptv-org/iptv) playlists hosted at
 * iptv-org.github.io. EPG URLs point at community XMLTV from iptv-epg.org where
 * available (matched via `tvg-id` on each channel).
 */
data class IptvOrgPreset(
    val id: String,
    val emoji: String,
    val nameRu: String,
    val nameEn: String,
    val playlistUrl: String,
    /** Stored on the M3U provider row; used by [syncXmltv] for M3U kind. */
    val epgUrl: String = "",
    val channelHintRu: String = "",
    val channelHintEn: String = "",
) {
    fun displayName(ru: Boolean) = if (ru) nameRu else nameEn
    fun channelHint(ru: Boolean) = if (ru) channelHintRu else channelHintEn
}

object IptvOrgCatalog {
    private const val BASE = "https://iptv-org.github.io/iptv"
    private const val EPG = "https://iptv-epg.org/files"

    val presets: List<IptvOrgPreset> = listOf(
        IptvOrgPreset(
            id = "ru",
            emoji = "🇷🇺",
            nameRu = "Россия",
            nameEn = "Russia",
            playlistUrl = "$BASE/countries/ru.m3u",
            epgUrl = "$EPG/epg-ru.xml.gz",
            channelHintRu = "Рекомендуем для старта",
            channelHintEn = "Recommended to start",
        ),
        IptvOrgPreset(
            id = "ua",
            emoji = "🇺🇦",
            nameRu = "Украина",
            nameEn = "Ukraine",
            playlistUrl = "$BASE/countries/ua.m3u",
            epgUrl = "$EPG/epg-ua.xml.gz",
        ),
        IptvOrgPreset(
            id = "by",
            emoji = "🇧🇾",
            nameRu = "Беларусь",
            nameEn = "Belarus",
            playlistUrl = "$BASE/countries/by.m3u",
            epgUrl = "$EPG/epg-by.xml.gz",
        ),
        IptvOrgPreset(
            id = "kz",
            emoji = "🇰🇿",
            nameRu = "Казахстан",
            nameEn = "Kazakhstan",
            playlistUrl = "$BASE/countries/kz.m3u",
        ),
        IptvOrgPreset(
            id = "rus-lang",
            emoji = "🗣️",
            nameRu = "На русском языке",
            nameEn = "Russian language",
            playlistUrl = "$BASE/languages/rus.m3u",
            epgUrl = "$EPG/epg-ru.xml.gz",
        ),
        IptvOrgPreset(
            id = "news",
            emoji = "📰",
            nameRu = "Новости",
            nameEn = "News",
            playlistUrl = "$BASE/categories/news.m3u",
            epgUrl = "$EPG/epg-ru.xml.gz",
        ),
        IptvOrgPreset(
            id = "sports",
            emoji = "⚽",
            nameRu = "Спорт",
            nameEn = "Sports",
            playlistUrl = "$BASE/categories/sports.m3u",
            epgUrl = "$EPG/epg-ru.xml.gz",
        ),
        IptvOrgPreset(
            id = "movies",
            emoji = "🎬",
            nameRu = "Кино",
            nameEn = "Movies",
            playlistUrl = "$BASE/categories/movies.m3u",
            epgUrl = "$EPG/epg-ru.xml.gz",
        ),
        IptvOrgPreset(
            id = "series",
            emoji = "📺",
            nameRu = "Сериалы",
            nameEn = "Series",
            playlistUrl = "$BASE/categories/series.m3u",
            epgUrl = "$EPG/epg-ru.xml.gz",
        ),
        IptvOrgPreset(
            id = "kids",
            emoji = "👶",
            nameRu = "Детские",
            nameEn = "Kids",
            playlistUrl = "$BASE/categories/kids.m3u",
            epgUrl = "$EPG/epg-ru.xml.gz",
        ),
        IptvOrgPreset(
            id = "music",
            emoji = "🎵",
            nameRu = "Музыка",
            nameEn = "Music",
            playlistUrl = "$BASE/categories/music.m3u",
            epgUrl = "$EPG/epg-ru.xml.gz",
        ),
        IptvOrgPreset(
            id = "documentary",
            emoji = "🎞️",
            nameRu = "Документальные",
            nameEn = "Documentary",
            playlistUrl = "$BASE/categories/documentary.m3u",
            epgUrl = "$EPG/epg-ru.xml.gz",
        ),
        IptvOrgPreset(
            id = "world",
            emoji = "🌍",
            nameRu = "Весь мир (большой)",
            nameEn = "World (large)",
            playlistUrl = "$BASE/index.m3u",
            channelHintRu = "Долгая загрузка на слабых приставках",
            channelHintEn = "Slow on low-end boxes",
        ),
    )

    const val SOURCE_URL = "https://github.com/iptv-org/iptv"
    const val LICENSE_NOTE_RU =
        "Публичные ссылки iptv-org. Контент — от правообладателей каналов; используйте на свой риск."
    const val LICENSE_NOTE_EN =
        "Public iptv-org links. Streams belong to channel owners; use at your own discretion."
}
