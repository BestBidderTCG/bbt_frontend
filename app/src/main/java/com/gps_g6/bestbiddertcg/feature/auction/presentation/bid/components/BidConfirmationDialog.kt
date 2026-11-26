
package com.gps_g6.bestbiddertcg.feature.auction.presentation.bid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.gps_g6.bestbiddertcg.R
import com.gps_g6.bestbiddertcg.core.ui.theme.BestBidderTCGTheme

@Composable
fun BidConfirmationDialog(
    bidAmount: Double,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 300.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = Color(0xFFFFEDD5),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bid_orange),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Text(
                    text = "¿Confirmar puja?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF263142)
                )

                Text(
                    text = "Vas a realizar una puja por la siguiente cantidad:",
                    fontSize = 14.sp,
                    color = Color(0xFF737D8D),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "%.2f €".format(bidAmount),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD0440A)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFFFEDD5),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ⓘ",
                        color = Color(0xFFD0440A),
                        fontSize = 18.sp
                    )

                    Text(
                        text = "Esta puja es irrevocable. Una vez confirmada, no podrás retirarla.",
                        fontSize = 12.sp,
                        color = Color(0xFF303846)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD0440A)
                    )
                ) {
                    Text(
                        text = "Confirmar puja",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(9.dp)
                ) {
                    Text(
                        text = "Volver y revisar",
                        fontSize = 14.sp,
                        color = Color(0xFFD0440A)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 700)
@Composable
private fun BidConfirmationDialogPreview() {
    BestBidderTCGTheme {
        Box(
            modifier = Modifier
                .background(Color(0xFF999999))
                .padding(16.dp)
        ) {
            BidConfirmationDialog(
                bidAmount = 46.00,
                onConfirm = {},
                onDismiss = {}
            )
        }
    }
}
