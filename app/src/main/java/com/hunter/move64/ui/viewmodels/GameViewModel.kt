package com.hunter.move64.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.hunter.move64.core.chess.getInitialBoard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class States {
    Normal,
    Highlighted,
    Move,
    Capture
}

class GameViewModel: ViewModel() {
    private val _board = MutableStateFlow(getInitialBoard())
    val board = _board.asStateFlow()

    private val _boardState = MutableStateFlow(List<States>(64) { States.Normal })
    val boardState = _boardState.asStateFlow()

    fun onSquareClick(index: Int) {
        val newState = _boardState.value.toMutableList()
        newState[index] = States.Highlighted
        _boardState.value = newState
    }
}