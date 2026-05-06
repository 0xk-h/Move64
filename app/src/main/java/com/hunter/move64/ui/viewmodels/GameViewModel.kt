package com.hunter.move64.ui.viewmodels

import android.R.attr.type
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hunter.move64.core.chess.Board
import com.hunter.move64.core.chess.GameState
import com.hunter.move64.core.chess.PieceType
import com.hunter.move64.core.chess.Pieces
import com.hunter.move64.core.chess.applyMove
import com.hunter.move64.core.chess.generateMoves
import com.hunter.move64.core.chess.getInitialBoard
import com.hunter.move64.data.model.GameType
import com.hunter.move64.domain.service.GameService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class States {
    Normal,
    Selected,
    Move,
    Capture
}

sealed class PromotionSquare {
    abstract val value: Pieces?

    object Cancel: PromotionSquare() {
        override val value = null
    }
    data class FromEnum(val type: Pieces): PromotionSquare() {
        override val value = type
    }

}

class GameViewModel(
    private val service: GameService
): ViewModel() {
    init {
        loadLastGame()
    }

    private val _board = MutableStateFlow(getInitialBoard())
    val board = _board.asStateFlow()

    private val _isHighlighted = MutableStateFlow(List(64) {false})
    val isHighlighted = _isHighlighted.asStateFlow()

    private val _boardState = MutableStateFlow(List(64) { States.Normal })
    val boardState = _boardState.asStateFlow()

    private val _promotionSquares = MutableStateFlow(List<PromotionSquare?>(64) { null })
    val promotionSquares = _promotionSquares.asStateFlow()

    private val _moves = MutableStateFlow(generateMoves(board.value).moves)

    private val _gameState = MutableStateFlow(GameState.Ongoing)
    val gameState = _gameState.asStateFlow()

    private var _promotionIndex: Int? = null

    var selectedSquare: Int? = null

    private fun loadLastGame() {
        viewModelScope.launch {
            _board.value = service.loadGame(GameType.PvP)
        }
    }

    private fun saveGame(type: GameType) {
        viewModelScope.launch {
            service.saveGame(type, board.value)
        }
    }

    fun applyStateChanges(newState: MutableList<States>, bb: ULong, state: States): MutableList<States> {
        var bb = bb
        while (bb != 0UL) {
            val i = java.lang.Long.numberOfTrailingZeros(bb.toLong())
            newState[i] = state
            bb = bb and (bb - 1u)
        }

        return newState
    }

    fun onSquareClick(index: Int, type: PieceType? = null) {
        val piece = board.value.getPiece(index)

        // apply the move and reset the board state
        if (type != null || boardState.value[index] == States.Move || boardState.value[index] == States.Capture) {
            // Eligible for promotion
            if (board.value.getPiece(selectedSquare!!)?.type == PieceType.Pawn && (index in 0..7 || index in 56..63)) {
                handlePromotion(index)
            } else {
                val targetIndex = if (type != null) _promotionIndex!! else index
                _board.value = applyMove(board.value, selectedSquare!!, targetIndex, type)
                _boardState.value = List(64) {States.Normal}
                _promotionSquares.value = List(64) { null }
                _promotionIndex = null
                val newHighlights = MutableList(64) {false}
                newHighlights[selectedSquare!!] = true
                newHighlights[targetIndex] = true
                _isHighlighted.value = newHighlights
                selectedSquare = null
                val res = generateMoves(board.value)
                _moves.value = res.moves
                _gameState.value = res.gameState

                saveGame(GameType.PvP)
                }
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

    fun handlePromotion(index: Int) {
        _promotionIndex = index
        val newPromotionSquare = MutableList<PromotionSquare?>(64) { null }
        if (index in 0..7) {
            newPromotionSquare[index] = PromotionSquare.FromEnum(Pieces.BlackQueen)
            newPromotionSquare[index + 8] = PromotionSquare.FromEnum(Pieces.BlackKnight)
            newPromotionSquare[index + 16] = PromotionSquare.FromEnum(Pieces.BlackRook)
            newPromotionSquare[index + 24] = PromotionSquare.FromEnum(Pieces.BlackBishop)
            newPromotionSquare[index + 32] = PromotionSquare.Cancel
        } else {
            newPromotionSquare[index] = PromotionSquare.FromEnum(Pieces.WhiteQueen)
            newPromotionSquare[index - 8] = PromotionSquare.FromEnum(Pieces.WhiteKnight)
            newPromotionSquare[index - 16] = PromotionSquare.FromEnum(Pieces.WhiteRook)
            newPromotionSquare[index - 24] = PromotionSquare.FromEnum(Pieces.WhiteBishop)
            newPromotionSquare[index - 32] = PromotionSquare.Cancel
        }

        _promotionSquares.value = newPromotionSquare
    }

    fun onCancel() {
        _promotionIndex = null
        _promotionSquares.value = List<PromotionSquare?>(64) { null }
    }

    fun reset() {
        _board.value = getInitialBoard()
        _boardState.value = List(64) { States.Normal }
        _isHighlighted.value = List(64) { false }
        _moves.value = generateMoves(board.value).moves
        _gameState.value = GameState.Ongoing
        _promotionSquares.value = List<PromotionSquare?>(64) { null }
        _promotionIndex = null
        selectedSquare = null

        saveGame(GameType.PvP)
    }
}