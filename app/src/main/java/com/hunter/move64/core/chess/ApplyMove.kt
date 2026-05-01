package com.hunter.move64.core.chess

fun applyMove(board: Board, from: Int, to: Int): Board {
    var board = board
    val piece = board.getPiece(from)
    val captured = board.getPiece(to)

    // updating customs
    board.isWhiteMove = !board.isWhiteMove
    board.enPassantSquare = null

    board = updateBoard(board, piece!!, 1UL shl from)
    board = updateBoard(board, piece, 1UL shl to)
    captured?.let {
        board = updateBoard(board, captured, (1UL shl to))
    }

    return board
}

fun updateBoard(board: Board, piece: Piece, mask: ULong): Board {
    return when (piece.color) {
        Color.White -> when (piece.type) {
            PieceType.Pawn -> board.copy(
                whitePawn = board.whitePawn xor mask
            )
            PieceType.Rook -> board.copy(
                whiteRook = board.whiteRook xor mask
            )
            PieceType.Knight -> board.copy(
                whiteKnight = board.whiteKnight xor mask
            )
            PieceType.Bishop -> board.copy(
                whiteBishop = board.whiteBishop xor mask
            )
            PieceType.Queen -> board.copy(
                whiteQueen = board.whiteQueen xor mask
            )
            PieceType.King -> board.copy(
                whiteKing = board.whiteKing xor mask
            )
        }

        Color.Black -> when (piece.type) {
            PieceType.Pawn -> board.copy(
                blackPawn = board.blackPawn xor mask
            )
            PieceType.Rook -> board.copy(
                blackRook = board.blackRook xor mask
            )
            PieceType.Knight -> board.copy(
                blackKnight = board.blackKnight xor mask
            )
            PieceType.Bishop -> board.copy(
                blackBishop = board.blackBishop xor mask
            )
            PieceType.Queen -> board.copy(
                blackQueen = board.blackQueen xor mask
            )
            PieceType.King -> board.copy(
                blackKing = board.blackKing xor mask
            )
        }
    }
}