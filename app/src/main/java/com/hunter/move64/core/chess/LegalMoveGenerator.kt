package com.hunter.move64.core.chess

import java.lang.Long.numberOfTrailingZeros

enum class GameState {
    Ongoing,
    Check,
    Checkmate,
    Stalemate,
    DrawByRepetition,
    DrawByInsufficientMaterial,
    DrawBy50MoveRule
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
        val index = numberOfTrailingZeros(pieces.toLong())
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
        isInsufficientMaterial(board) -> GameState.DrawByInsufficientMaterial
        board.halfMoveClock >= 100 -> GameState.DrawBy50MoveRule
        isThreefoldRepetition(board) -> GameState.DrawByRepetition
        inCheck -> GameState.Check
        else -> GameState.Ongoing
    }

    return TurnResult(
        moves = moves,
        gameState = gameState
    )
}

fun isThreefoldRepetition(board: Board): Boolean {
    val currentHash = board.zobristHash
    var count = 0
    for (hash in board.history) {
        if (hash == currentHash) {
            count++
        }
    }
    return count >= 3
}

fun isKingInCheck(board: Board, color: Color): Boolean {
    val kingSquare = if (color == Color.White) board.whiteKing else board.blackKing
    val kingIndex = numberOfTrailingZeros(kingSquare.toLong())
    return board.isAttacked(kingIndex, color)
}

fun generateLegalMoves(board: Board, from: Int, pseudoMoves: Output, color: Color): Output {
    var legalMoves = 0UL
    var legalCaptures = 0UL

    var movesToVerify = pseudoMoves.moves
    while (movesToVerify != 0UL) {
        val to = numberOfTrailingZeros(movesToVerify.toLong())
        val tempBoard = applyMove(board, from, to, null, updateHash = false)
        if (!isKingInCheck(tempBoard, color)) {
            legalMoves = legalMoves or (1UL shl to)
        }
        movesToVerify = movesToVerify and (movesToVerify - 1u)
    }

    var capturesToVerify = pseudoMoves.captures
    while (capturesToVerify != 0UL) {
        val to = numberOfTrailingZeros(capturesToVerify.toLong())
        val tempBoard = applyMove(board, from, to, null, updateHash = false)
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

fun isInsufficientMaterial(board: Board): Boolean {
    val pieces = board.allPiecesWithoutKings()
    val size = pieces.toLong().countOneBits()

    if (size == 0) return true // King vs King

    if (size == 1) {
        // King + Minor vs King
        val minorPieces = board.whiteKnight or board.whiteBishop or board.blackKnight or board.blackBishop
        return (pieces and minorPieces) != 0UL
    }

    if (size == 2) {
        // King + Bishop vs King + Bishop (if they are on the same color square)
        if (board.whiteBishop != 0UL && board.blackBishop != 0UL) {
            val wBishopIdx = numberOfTrailingZeros(board.whiteBishop.toLong())
            val bBishopIdx = numberOfTrailingZeros(board.blackBishop.toLong())
            val wColor = (wBishopIdx / 8 + wBishopIdx % 8) % 2
            val bColor = (bBishopIdx / 8 + bBishopIdx % 8) % 2
            return wColor == bColor
        }

        // King + Knight vs King + Knight
        if (board.whiteKnight != 0UL && board.blackKnight != 0UL) {
            return true
        }
    }

    return false
}