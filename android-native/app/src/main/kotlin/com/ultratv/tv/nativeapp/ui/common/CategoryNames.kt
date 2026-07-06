package com.ultratv.tv.nativeapp.ui.common

/**
 * Display names for IPTV group-title values (iptv-org / M3U). Raw values stay in DB.
 */
private val CATEGORY_RU = mapOf(
    "News" to "Новости",
    "Sports" to "Спорт",
    "Movies" to "Кино",
    "Series" to "Сериалы",
    "Kids" to "Детские",
    "Music" to "Музыка",
    "Documentary" to "Документальные",
    "Entertainment" to "Развлечения",
    "General" to "Общие",
    "Religious" to "Религия",
    "Education" to "Образование",
    "Shop" to "Магазины",
    "Outdoor" to "Природа",
    "Weather" to "Погода",
    "Auto" to "Авто",
    "Animation" to "Анимация",
    "Classic" to "Классика",
    "Comedy" to "Комедия",
    "Cooking" to "Кулинария",
    "Culture" to "Культура",
    "Family" to "Семейные",
    "Legislative" to "Парламент",
    "Lifestyle" to "Образ жизни",
    "Local" to "Местные",
    "Relax" to "Релакс",
    "Science" to "Наука",
    "Travel" to "Путешествия",
    "Undefined" to "Разное",
    "Public" to "Общественные",
    "Business" to "Бизнес",
    "Russian" to "Русские",
    "Russia" to "Россия",
    "Ukraine" to "Украина",
    "Belarus" to "Беларусь",
    "Kazakhstan" to "Казахстан",
)

private val CATEGORY_RU_PARTIAL = listOf(
    "news" to "Новости",
    "sport" to "Спорт",
    "movie" to "Кино",
    "film" to "Кино",
    "series" to "Сериалы",
    "serial" to "Сериалы",
    "kids" to "Детские",
    "children" to "Детские",
    "music" to "Музыка",
    "document" to "Документальные",
    "entertain" to "Развлечения",
    "general" to "Общие",
    "relig" to "Религия",
    "education" to "Образование",
    "shop" to "Магазины",
    "culture" to "Культура",
    "family" to "Семейные",
    "science" to "Наука",
    "travel" to "Путешествия",
    "russia" to "Россия",
    "russian" to "Русские",
    "ukraine" to "Украина",
    "kazakh" to "Казахстан",
)

fun displayCategoryName(raw: String, russianUi: Boolean): String {
    val pretty = prettyCategoryName(raw)
    if (!russianUi || pretty.isBlank()) return pretty
    CATEGORY_RU[pretty]?.let { return it }
    CATEGORY_RU.entries.firstOrNull { (en, _) ->
        pretty.equals(en, ignoreCase = true)
    }?.value?.let { return it }
    val lower = pretty.lowercase()
    CATEGORY_RU_PARTIAL.firstOrNull { (key, _) -> key in lower }?.second?.let { return it }
    return pretty
}
