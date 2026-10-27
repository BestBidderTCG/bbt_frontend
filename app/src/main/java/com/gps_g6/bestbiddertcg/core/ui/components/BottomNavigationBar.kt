
package com.gps_g6.bestbiddertcg.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.app.AppPage
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme
import com.gps_g6.bestbiddertcg.core.ui.theme.Surface
import com.gps_g6.bestbiddertcg.core.ui.theme.TextSecondary

@Composable
fun BottomNavigationBar(
    selectedPage: AppPage,
    onPageSelected: (AppPage) -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Surface,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(
            page = AppPage.EXPLORE,
            label = "Explorar",
            selectedPage = selectedPage,
            activeIcon = R.drawable.ic_explore_active,
            inactiveIcon = R.drawable.ic_explore_inactive,
            onClick = onPageSelected
        )

        NavigationItem(
            page = AppPage.SOCIAL,
            label = "Social",
            selectedPage = selectedPage,
            activeIcon = R.drawable.ic_social_active,
            inactiveIcon = R.drawable.ic_social_inactive,
            onClick = onPageSelected
        )

        Image(
            painter = painterResource(R.drawable.ic_create_button),
            contentDescription = "Crear subasta",
            modifier = Modifier
                .width(72.dp)
                .height(48.dp)
                .clickable(
                    role = Role.Button,
                    onClick = onAddClick
                )
        )

        NavigationItem(
            page = AppPage.FAVORITES,
            label = "Favoritos",
            selectedPage = selectedPage,
            activeIcon = R.drawable.ic_favorites_active,
            inactiveIcon = R.drawable.ic_favorites_inactive,
            onClick = onPageSelected
        )

        NavigationItem(
            page = AppPage.PROFILE,
            label = "Perfil",
            selectedPage = selectedPage,
            activeIcon = R.drawable.ic_profile_active,
            inactiveIcon = R.drawable.ic_profile_inactive,
            onClick = onPageSelected
        )
    }
}

@Composable
private fun RowScope.NavigationItem(
    page: AppPage,
    label: String,
    selectedPage: AppPage,
    activeIcon: Int,
    inactiveIcon: Int,
    onClick: (AppPage) -> Unit
) {
    val isSelected = selectedPage == page

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Tab,
                onClick = { onClick(page) }
            )
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(
                if (isSelected) activeIcon else inactiveIcon
            ),
            contentDescription = label,
            modifier = Modifier.size(28.dp)
        )

        Text(
            text = label,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                TextSecondary
            },
            fontSize = 14.sp,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarPreview() {
    BestBidderTCGTheme {
        BottomNavigationBar(
            selectedPage = AppPage.EXPLORE,
            onPageSelected = {},
            onAddClick = {}
        )
    }
}
