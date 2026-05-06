package com.hunter.move64.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hunter.move64.core.chess.GameState

@Composable
fun DrawPopup(
    reason: GameState,
    onPlayAgain: () -> Unit,
    onBack: () -> Unit
) {
    GameOverlay {
        PopupCard {

            // TODO: to make it svg or png
            Text("🤝", fontSize = 40.sp)

            Spacer(Modifier.height(8.dp))

            Text(
                "DRAW!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(12.dp))

            when (reason) {
                GameState.Stalemate -> DrawReason("Stalemate", "No legal moves for current player")
                GameState.DrawByRepetition -> DrawReason("Threefold Repetition", "The position repeated 3 times")
                GameState.DrawByInsufficientMaterial -> DrawReason("Insufficient Material", "No checkmate is possible")
                GameState.DrawBy50MoveRule -> DrawReason("50-Move Rule", "No pawn moves or captures in 50 moves")
                else -> DrawReason("Draw", "Game ended in a draw")
            }

            Spacer(Modifier.height(20.dp))

            PopupButton("Play Again", Color(0xFF3B82F6), onPlayAgain)

            Spacer(Modifier.height(10.dp))

            PopupButton("Back to Menu", Color(0xFF2A2F36), onBack)
        }
    }
}

@Composable
fun DrawReason(title: String, desc: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, color = Color.White, fontWeight = FontWeight.Medium)
        Text(desc, color = Color.Gray, fontSize = 12.sp)
    }
}