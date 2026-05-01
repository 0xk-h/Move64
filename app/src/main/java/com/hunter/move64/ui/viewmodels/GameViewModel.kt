package com.hunter.move64.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.hunter.move64.core.chess.applyMove
import com.hunter.move64.core.chess.generateMoves
import com.hunter.move64.core.chess.getInitialBoard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class States {
    Normal,
    Highlighted,
    Selected,
    Move,
    Capture
}

private fun removeSelectedSquare(state: MutableList<States>): MutableList<States> {
    return state.map {
        if (it != States.Highlighted) States.Normal else it
    }.toMutableList()
}

class GameViewModel: ViewModel() {
    private val _board = MutableStateFlow(getInitialBoard())
    val board = _board.asStateFlow()

    private val _boardState = MutableStateFlow(List(64) { States.Normal })
    val boardState = _boardState.asStateFlow()

    var selectedSquare: Int? = null

    fun applyStateChanges(newState: MutableList<States>, bb: ULong, state: States): MutableList<States> {
        var bb = bb
        while (bb != 0UL) {
            val i = java.lang.Long.numberOfTrailingZeros(bb.toLong())
            newState[i] = state
            bb = bb and (bb - 1u)
        }

        return newState
    }

    fun onSquareClick(index: Int) {
        val piece = board.value.getPiece(index)

        // apply the move and reset the board state
        if (boardState.value[index] == States.Move || boardState.value[index] == States.Capture) {
            _board.value = applyMove(board.value, selectedSquare!!, index)
            val newState = MutableList(64) {States.Normal}
            newState[selectedSquare!!] = States.Highlighted
            newState[index] = States.Highlighted
            _boardState.value = newState
            selectedSquare = null
        }

        // Selected invalid so nothing is selected
        else if (piece == null || piece.color != board.value.getCurrPlayer()) {
            selectedSquare?.let {
                _boardState.value = removeSelectedSquare(_boardState.value.toMutableList())
            }
        }

        // selected a valid piece remove any previous selected ones
        else {
            val newState = removeSelectedSquare(_boardState.value.toMutableList())
            selectedSquare = index
            newState[index] = States.Selected

            val res = generateMoves(board.value, index)
            applyStateChanges(newState, res.moves, States.Move)
            applyStateChanges(newState, res.captures, States.Capture)

            _boardState.value = newState
        }
    }

    fun reset() {
        _board.value = getInitialBoard()
        _boardState.value = List(64) { States.Normal }

        selectedSquare = null
    }
}