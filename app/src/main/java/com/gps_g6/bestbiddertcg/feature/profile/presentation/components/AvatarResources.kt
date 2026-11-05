package com.gps_g6.bestbiddertcg.feature.profile.presentation.components

import androidx.annotation.DrawableRes
import com.gps_g6.bestbiddertcg.R

object AvatarResources {

    const val DEFAULT_AVATAR = 1

    @DrawableRes
    fun getDrawable(avatarNum: Int?): Int {
        return when (avatarNum) {
            1 -> R.drawable.sample_piplup_avatar
            2 -> R.drawable.sample_piplup_avatar
            3 -> R.drawable.sample_piplup_avatar
            4 -> R.drawable.sample_piplup_avatar
            5 -> R.drawable.sample_piplup_avatar
            6 -> R.drawable.sample_piplup_avatar
            7 -> R.drawable.sample_piplup_avatar
            8 -> R.drawable.sample_piplup_avatar
            9 -> R.drawable.sample_piplup_avatar
            else -> R.drawable.sample_piplup_avatar
        }
    }
}
