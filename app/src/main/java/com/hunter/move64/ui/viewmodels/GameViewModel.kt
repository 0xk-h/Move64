package com.hunter.move64.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.hunter.move64.core.chess.Board
import com.hunter.move64.core.chess.getInitialBoard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel: ViewModel() {
    private val _board = MutableStateFlow(getInitialBoard())
    val board = _board.asStateFlow()
}