package com.hunter.move64.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CheckmatePopup(
    winner: String,
    onPlayAgain: () -> Unit,
    onBack: () -> Unit
) {
    GameOverlay {
        PopupCard {

            // TODO: should make it png or svg
            Text("👑", fontSize = 40.sp)

            Spacer(Modifier.height(8.dp))

            Text(
                "CHECKMATE!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                "$winner wins",
                color = Color(0xFFFFD700),
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "No legal moves remaining.",
                color = Color.LightGray,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(20.dp))

            PopupButton("Play Again", Color(0xFF3B82F6), onPlayAgain)

            Spacer(Modifier.height(10.dp))

            PopupButton("Back to Menu", Color(0xFF2A2F36), onBack)
        }
    }
}