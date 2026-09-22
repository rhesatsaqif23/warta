package com.rhesdev.warta.navigation

object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val DETAIL = "detail/{newsLink}"
    const val SEARCH = "search"
    const val CATEGORY = "category"
    const val PROFILE = "profile"

    fun detail(newsLink: String): String = "detail/$newsLink"
}
