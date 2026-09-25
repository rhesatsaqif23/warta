package com.rhesdev.warta.navigation

import android.content.Context
import com.rhesdev.warta.core.utils.ITEM_EXTRA_CATEGORY
import com.rhesdev.warta.core.utils.ITEM_EXTRA_LINK
import com.rhesdev.warta.core.utils.ITEM_EXTRA_QUERY
import com.rhesdev.warta.feature.news.presentation.category.CategoryActivity
import com.rhesdev.warta.feature.news.presentation.detail.DetailActivity
import com.rhesdev.warta.feature.news.presentation.home.HomeActivity
import com.rhesdev.warta.feature.news.presentation.search.SearchActivity
import com.rhesdev.warta.feature.profile.presentation.ProfileActivity
import splitties.activities.start

// Shield class unifying activity launches behind type-safe intent navigation.
object WartaNavigator {
    fun openHome(context: Context) = context.start<HomeActivity>()

    fun openDetail(context: Context, link: String) =
        context.start<DetailActivity> { putExtra(ITEM_EXTRA_LINK, link) }

    fun openSearch(context: Context, query: String? = null) =
        context.start<SearchActivity> {
            if (!query.isNullOrBlank()) putExtra(ITEM_EXTRA_QUERY, query)
        }

    fun openCategory(context: Context, categoryName: String? = null) =
        context.start<CategoryActivity> {
            if (!categoryName.isNullOrBlank()) putExtra(ITEM_EXTRA_CATEGORY, categoryName)
        }

    fun openProfile(context: Context) = context.start<ProfileActivity>()
}