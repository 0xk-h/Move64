package com.hunter.move64.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.hunter.move64.core.chess.GameState
import com.hunter.move64.core.chess.applyMove
import com.hunter.move64.core.chess.generateMoves
import com.hunter.move64.core.chess.getInitialBoard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class States {
    Normal,
    Selected,
    Move,
    Capture
}

class GameViewModel: ViewModel() {
    private val _board = MutableStateFlow(getInitialBoard())
    val board = _board.asStateFlow()

    private val _isHighlighted = MutableStateFlow(List(64) {false})
    val isHighlighted = _isHighlighted.asStateFlow()

    private val _boardState = MutableStateFlow(List(64) { States.Normal })
    val boardState = _boardState.asStateFlow()

    private val _moves = MutableStateFlow(generateMoves(board.value).moves)

    private val _gameState = MutableStateFlow(GameState.Ongoing)
    val gameState = _gameState.asStateFlow()

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
            _board.value = applyMove(board.value, selectedSquare!!, index, null)
            _boardState.value = List(64) {States.Normal}
            val newHighlights = MutableList(64) {false}
            newHighlights[selectedSquare!!] = true
            newHighlights[index] = true
            _isHighlighted.value = newHighlights
            selectedSquare = null
            val res = generateMoves(board.value)
            _moves.value = res.moves
            _gameState.value = res.gameState
        }

        // Selected invalid so nothing is selected
        else if (piece == null || piece.color != board.value.getCurrPlayer()) {
            selectedSquare?.let {
                _boardState.value = List(64) {States.Normal}
                selectedSquare = null
            }
        }

        // selected a valid piece remove any previous selected ones
        else {
            val newState = MutableList(64) {States.Normal}
            selectedSquare = index
            newState[index] = States.Selected

            val res = _moves.value[index]
            applyStateChanges(newState, res.moves, States.Move)
            applyStateChanges(newState, res.captures, States.Capture)

            _boardState.value = newState
        }
    }

    fun reset() {
        _board.value = getInitialBoard()
        _boardState.value = List(64) { States.Normal }
        _isHighlighted.value = List(64) { false }
        _moves.value = generateMoves(board.value).moves
        _gameState.value = GameState.Ongoing

        selectedSquare = null
    }
}