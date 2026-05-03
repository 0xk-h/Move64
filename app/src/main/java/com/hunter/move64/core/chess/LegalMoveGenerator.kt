package com.hunter.move64.core.chess


enum class GameState {
    Ongoing,
    Check,
    Checkmate,
    Stalemate,
    DrawByRepetition,
    DrawByInsufficientMaterial
}

data class TurnResult (
    val moves: List<Output>,
    val gameState: GameState
)

fun generateMoves(board: Board): TurnResult {
    val moves = MutableList(64){ Output() }
    val color = if (board.isWhiteMove) Color.White else Color.Black
    var hasLegalMoves = false

    var pieces = if (board.isWhiteMove) board.whitePieces else board.blackPieces
    while (pieces != 0UL) {
        val index = java.lang.Long.numberOfTrailingZeros(pieces.toLong())
        val pseudoMoves = generatePseudoMoves(board, index)
        val legalMoves = generateLegalMoves(board, index, pseudoMoves, color)
        if (!legalMoves.isEmpty) {
            hasLegalMoves = true
        }
        moves[index] = legalMoves
        pieces = pieces and (pieces - 1u)
    }

    val inCheck = isKingInCheck(board, color)
    val gameState = when {
        !hasLegalMoves && inCheck -> GameState.Checkmate
        !hasLegalMoves && !inCheck -> GameState.Stalemate
        inCheck -> GameState.Check
        else -> GameState.Ongoing
    }

    return TurnResult(
        moves = moves,
        gameState = gameState
    )
}

fun isKingInCheck(board: Board, color: Color): Boolean {
    val kingSquare = if (color == Color.White) board.whiteKing else board.blackKing
    val kingIndex = java.lang.Long.numberOfTrailingZeros(kingSquare.toLong())
    return board.isAttacked(kingIndex, color)
}

fun generateLegalMoves(board: Board, from: Int, pseudoMoves: Output, color: Color): Output {
    var legalMoves = 0UL
    var legalCaptures = 0UL

    var movesToVerify = pseudoMoves.moves
    while (movesToVerify != 0UL) {
        val to = java.lang.Long.numberOfTrailingZeros(movesToVerify.toLong())
        val tempBoard = applyMove(board, from, to, null)
        if (!isKingInCheck(tempBoard, color)) {
            legalMoves = legalMoves or (1UL shl to)
        }
        movesToVerify = movesToVerify and (movesToVerify - 1u)
    }

    var capturesToVerify = pseudoMoves.captures
    while (capturesToVerify != 0UL) {
        val to = java.lang.Long.numberOfTrailingZeros(capturesToVerify.toLong())
        val tempBoard = applyMove(board, from, to, null)
        if (!isKingInCheck(tempBoard, color)) {
            legalCaptures = legalCaptures or (1UL shl to)
        }
        capturesToVerify = capturesToVerify and (capturesToVerify - 1u)
    }

    return Output(
        moves = legalMoves,
        captures = legalCaptures
    )
}