package com.hunter.move64.core.chess

data class Output (
    val captures: ULong = 0UL,
    val moves: ULong = 0UL
)

fun generateMoves(board: Board, index: Int): Output {
    val piece = board.getPiece(index)

    return when (piece?.type) {
        PieceType.Pawn -> generatePawnMoves(board, index, piece.color)
        PieceType.Rook -> generateRookMoves(board, index, piece.color)
        PieceType.Knight -> generateKnightMoves(board, index, piece.color)
        PieceType.Bishop -> generateBishopMoves(board, index, piece.color)
        PieceType.Queen -> generateQueenMoves(board, index, piece.color)
        PieceType.King -> generateKingMoves(board, index, piece.color)
        null -> Output()
    }
}

const val NOT_A_FILE = 0xFEFEFEFEFEFEFEFEUL
const val NOT_H_FILE = 0x7F7F7F7F7F7F7F7FUL
fun generatePawnMoves(board: Board, index: Int, color: Color): Output {
    if (index !in 8..<56) {
        return Output()
    }
    var captures = 0UL
    var moves = 0UL

    val occupied = board.occupied
    val i = 1UL shl index
    val enemies = if (color == Color.White) board.blackPieces else board.whitePieces

    if (color == Color.White) {
        val oneStep = i shl 8
        if (oneStep and occupied == 0UL) {
            moves = moves or oneStep
            if (index in 8..15) {
                val twoStep = oneStep shl 8
                if (twoStep and occupied == 0UL) {
                    moves = moves or twoStep
                }
            }
        }

        val leftCapture = i shl 7 and enemies and NOT_H_FILE
        val rightCapture = i shl 9 and enemies and NOT_A_FILE
        captures = captures or leftCapture or rightCapture

    } else {
        val oneStep = i shr 8
        if (oneStep and occupied == 0UL) {
            moves = moves or oneStep
            if (index in 48..55) {
                val twoStep = oneStep shr 8
                if (twoStep and occupied == 0UL) {
                    moves = moves or twoStep
                }
            }
        }

        val leftCapture = i shr 7 and enemies and NOT_A_FILE
        val rightCapture = i shr 9 and enemies and NOT_H_FILE
        captures = captures or leftCapture or rightCapture
    }

    return Output(captures = captures, moves = moves)
}

fun generateRookMoves(board: Board, index: Int, color: Color): Output {
    val occupied = board.occupied
    val reachable = rookMoves(index, occupied)
    val enemies = if (color == Color.White) board.blackPieces else board.whitePieces

    val moves = reachable and occupied.inv()
    val captures = reachable and enemies
    return Output(captures = captures, moves = moves)
}

fun generateKnightMoves(board: Board, index: Int, color: Color): Output {
    return Output()
}

fun generateBishopMoves(board: Board, index: Int, color: Color): Output {
    return Output()
}

fun generateQueenMoves(board: Board, index: Int, color: Color): Output {
    return Output()
}

fun generateKingMoves(board: Board, index: Int, color: Color): Output {
    return Output()
}