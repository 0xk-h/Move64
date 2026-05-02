package com.hunter.move64.core.chess

fun generateMoves(board: Board): List<Output?> {
    val moves = MutableList<Output?>(64){ null }

    var pieces = if (board.isWhiteMove) board.whitePieces else board.blackPieces
    while (pieces != 0UL) {
        val index = java.lang.Long.numberOfTrailingZeros(pieces.toLong())
        moves[index] = generatePseudoMoves(board, index)
        pieces = pieces and (pieces - 1u)
    }

    return moves
}