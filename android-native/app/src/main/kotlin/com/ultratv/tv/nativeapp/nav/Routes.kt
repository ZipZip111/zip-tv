package com.ultratv.tv.nativeapp.nav

object Routes {
    const val HOME = "home"
    const val LIVE = "live"
    const val MOVIES = "movies"
    const val MOVIE_DETAIL = "movies/{id}"
    const val SERIES = "series"
    const val SERIES_DETAIL = "series/{id}"
    const val SEARCH = "search"
    const val GUIDE = "guide"
    const val FAVORITES = "favorites"
    const val SETTINGS = "settings"
    /** Stream URL lives in [PlaybackContext] — never in nav args (URLs are too long). */
    const val PLAYER = "player"

    fun movieDetail(id: Long) = "movies/$id"
    fun seriesDetail(id: Long) = "series/$id"
}
