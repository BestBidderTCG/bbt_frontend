package com.gps_g6.bestbiddertcg.feature.profile.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.*

@Composable
fun AvatarItem(
    avatarNum: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AppCornerRadius.medium),
        color = if (selected) PrimaryLight else Surface,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) Primary else Border
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.sample_piplup_avatar),
                contentDescription = "Avatar $avatarNum",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AvatarItemPreview() {
    BestBidderTCGTheme {
        Row(
            modifier = Modifier.padding(AppSpacing.l),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.m)
        ) {
            AvatarItem(
                avatarNum = 1,
                selected = true,
                onClick = {},
                modifier = Modifier.weight(1f)
            )

            AvatarItem(
                avatarNum = 2,
                selected = false,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}
