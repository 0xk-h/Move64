package com.hunter.move64.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hunter.move64.core.chess.GameState
import com.hunter.move64.ui.components.CheckmatePopup
import com.hunter.move64.ui.components.ChessBoard
import com.hunter.move64.ui.components.DrawPopup
import com.hunter.move64.ui.theme.Move64Theme
import com.hunter.move64.ui.viewmodels.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    onBackClick: () -> Unit,
    vm: GameViewModel = viewModel()
) {
    val board by vm.board.collectAsState()
    val grid = board.toGrid()
    val boardState by vm.boardState.collectAsState()
    val isHighlighted by vm.isHighlighted.collectAsState()
    val gameState by vm.gameState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (board.isWhiteMove) "White's turn" else "Black's turn"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)

                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { vm.reset() },
                        modifier = Modifier.padding(4.dp)
                    )
                    {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Player 1"
                )
                Spacer(modifier = Modifier.size(32.dp))

                ChessBoard(
                    grid,
                    boardState,
                    isHighlighted,
                    onSquareClick = vm::onSquareClick
                )

                Spacer(modifier = Modifier.size(32.dp))

                Text(
                    text = "Player 2"
                )
            }

            if (gameState != GameState.Ongoing && gameState != GameState.Check) {
                if (gameState == GameState.Checkmate) {
                    CheckmatePopup(
                        winner = if(board.isWhiteMove) "Black" else "White",
                        onPlayAgain = vm::reset,
                        onBack = onBackClick
                    )
                } else {
                    DrawPopup(
                        reason = gameState,
                        onPlayAgain = vm::reset,
                        onBack = onBackClick
                    )

                }
            }
        }
    }
}


@Preview
@Composable
fun GameScreenPreview() {
    Move64Theme(darkTheme = true) {
        GameScreen(
            onBackClick = {}
        )
    }
}