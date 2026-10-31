package com.gps_g6.bestbiddertcg.feature.profile.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gps_g6.bestbiddertcg.core.ui.theme.*
import com.gps_g6.bestbiddertcg.feature.profile.presentation.ProfileUiState
import com.gps_g6.bestbiddertcg.feature.profile.presentation.ProfileSampleData
import com.gps_g6.bestbiddertcg.R

@Composable
fun ProfileHeader(
    profile: ProfileUiState,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppCornerRadius.medium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.l),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    AppSpacing.m
                )
            ) {
                ProfileAvatar()

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        AppSpacing.s
                    )
                ) {
                    Text(
                        text = profile.username,
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStat(
                            value = profile.cardsCount,
                            label = stringResource(R.string.profile_cards),
                            modifier = Modifier.weight(1f)
                        )

                        ProfileStat(
                            value = profile.auctionsCount,
                            label = stringResource(R.string.profile_auctions),
                            modifier = Modifier.weight(1f)
                        )

                        ProfileStat(
                            value = profile.bidsCount,
                            label = stringResource(R.string.profile_bids),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.s))

            Text(
                text = profile.email,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(AppSpacing.m))

            OutlinedButton(
                onClick = onEditClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(
                    AppCornerRadius.medium
                ),
                border = BorderStroke(1.dp, Primary),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Primary
                )
            ) {
                Text(
                    text = stringResource(R.string.profile_edit),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}


@Composable
private fun ProfileAvatar() {
    Box(
        modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .background(PrimaryLight),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(
                R.drawable.ic_profile_active
            ),
            contentDescription = stringResource(R.string.profile_avatar_description),
            modifier = Modifier.size(44.dp),
            tint = Primary
        )
    }
}


@Composable
private fun ProfileStat(
    value: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            AppSpacing.xs
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            maxLines = 1
        )

        Text(
            text = value.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileHeaderPreview() {
    BestBidderTCGTheme {
        Box(
            modifier = Modifier
                .background(Background)
                .padding(AppSpacing.l)
        ) {
            ProfileHeader(
                profile = ProfileSampleData.profile,
                onEditClick = {}
            )
        }
    }
}