package com.hunter.move64.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hunter.move64.R
import com.hunter.move64.core.chess.Pieces
import com.hunter.move64.ui.viewmodels.States

object ChessColors {
    val black = Color(0xFFC2A779)
    val white = Color(0xFF765637)
    val highlightsW = Color(0xFFA27635)
    val highlightsB = Color(0xFFB48A3D)
    val none = Color(0x00FFFFFF)
}

@Composable
fun ChessBoard(
    grid: List<Pieces?>,
    boardState: List<States>,
    onSquareClick: (Int) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(10.dp)
    ) {
        val squareSize = maxWidth / 8

        Column() {
            for (i in 0..7) {
                Row() {
                    for (j in 0..7) {
                        val index: Int = (7 - i) * 8 + j

                        Box(
                            modifier = Modifier
                                .size(squareSize)
                                .background(
                                    if ((i + j) % 2 == 0) {
                                        ChessColors.black
                                    } else {
                                        ChessColors.white
                                    }
                                )
                                .background(
                                    if (boardState[index] == States.Highlighted || boardState[index] == States.Selected) {
                                        if ((i + j) % 2 == 0) ChessColors.highlightsB else ChessColors.highlightsW
                                    } else {
                                        ChessColors.none
                                    }
                                )
                                .clickable {
                                    onSquareClick(index)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            val piece = grid[index]

                            piece?.let {
                                Image(
                                    painter = painterResource(id = getDrawable(it)),
                                    contentDescription = it.name,
                                    modifier = Modifier.size(squareSize * 0.8f)
                                )
                            }

                            if (boardState[index] == States.Move) {
                                Box(
                                    modifier = Modifier
                                        .size(squareSize * 0.3f)
                                        .background(
                                            Color.Black.copy(alpha = 0.4f),
                                            CircleShape
                                        )
                                )
                            }

                            if (boardState[index] == States.Capture) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(squareSize * 0.05f)
                                        .border(
                                            3.dp,
                                            Color.Black.copy(alpha = 0.4f),
                                            CircleShape
                                        )
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}

fun getDrawable(piece: Pieces): Int {
    return when(piece) {
        Pieces.WhiteKing -> R.drawable.wk
        Pieces.WhiteQueen -> R.drawable.wq
        Pieces.WhiteRook -> R.drawable.wr
        Pieces.WhiteBishop -> R.drawable.wb
        Pieces.WhiteKnight -> R.drawable.wn
        Pieces.WhitePawn -> R.drawable.wp

        Pieces.BlackKing -> R.drawable.bk
        Pieces.BlackQueen -> R.drawable.bq
        Pieces.BlackRook -> R.drawable.br
        Pieces.BlackBishop -> R.drawable.bb
        Pieces.BlackKnight -> R.drawable.bn
        Pieces.BlackPawn -> R.drawable.bp
    }
}