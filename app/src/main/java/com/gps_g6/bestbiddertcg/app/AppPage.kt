package com.gps_g6.bestbiddertcg.app

import androidx.annotation.StringRes
import com.gps_g6.bestbiddertcg.R

/**
 * Defines the main pages available in the application
 */
enum class AppPage(
    @StringRes val titleRes: Int
) {
    EXPLORE(R.string.main_explore_title),
    SOCIAL(R.string.main_social_title),
    FAVORITES(R.string.main_favorites_title),
    PROFILE(R.string.main_profile_title)
}
