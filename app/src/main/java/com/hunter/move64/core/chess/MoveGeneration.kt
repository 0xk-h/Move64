package com.hunter.move64.core.chess

data class Output (
    val captures: ULong = 0UL,
    val moves: ULong = 0UL
)

fun generateMoves(board: Board, index: Int): Output {
    return Output()
}