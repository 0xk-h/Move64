package com.hunter.move64.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import com.hunter.move64.ui.components.ChessBoard
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = "Bot"
            )
            Spacer(modifier = Modifier.size(32.dp))

            ChessBoard(
                grid,
                boardState,
                onSquareClick = vm::onSquareClick
            )

            Spacer(modifier = Modifier.size(32.dp))

            Text(
                text = "Player 1"
            )
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