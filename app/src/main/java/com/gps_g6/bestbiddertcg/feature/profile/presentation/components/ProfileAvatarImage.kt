package com.gps_g6.bestbiddertcg.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.Primary
import com.gps_g6.bestbiddertcg.core.ui.theme.PrimaryLight

@Composable
fun ProfileAvatarImage(
    avatarNum: Int?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(PrimaryLight),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(
                AvatarResources.getDrawable(avatarNum)
            ),
            contentDescription = stringResource(
                R.string.profile_avatar_description
            ),
            modifier = Modifier.fillMaxSize(0.6f),
            tint = Primary
        )
    }
}
