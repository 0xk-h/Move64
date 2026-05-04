package com.hunter.move64.core.chess

import kotlin.math.abs

fun applyMove(board: Board, from: Int, to: Int, promotion: PieceType?, updateHash: Boolean = true): Board {
    var nextBoard = board
    val piece = board.getPiece(from) ?: return board
    val captured = board.getPiece(to)

    // if any enPassant capture occurs (only deleting the captured pawn)
    board.enPassantSquare?.let { epSquare ->
        if (to == epSquare && piece.type == PieceType.Pawn) {
            val capturedPawnPos = if (piece.color == Color.White) to - 8 else to + 8
            nextBoard = updateBoard(nextBoard, Piece(PieceType.Pawn, piece.color.opposite), 1UL shl capturedPawnPos)
        }
    }

    // handle capture
    captured?.let {
        nextBoard = updateBoard(nextBoard, it, 1UL shl to)
    }

    // just some customs
    nextBoard = updateGameState(nextBoard, from, to, piece, captured)

    // remove from previous position
    nextBoard = updateBoard(nextBoard, piece, 1UL shl from)

    // add it in the new position
    nextBoard = if (promotion != null) {
        updateBoard(nextBoard, Piece(promotion, piece.color), 1UL shl to)
    } else {
        updateBoard(nextBoard, piece, 1UL shl to)
    }

    // if castling happens should move the rook
    if (piece.type == PieceType.King && abs(from - to) == 2) {
        if (to < from) { // Queen Side Castling
            val rookFrom = if (piece.color == Color.White) 0 else 56
            val rookTo = if (piece.color == Color.White) 3 else 59
            val rook = Piece(PieceType.Rook, piece.color)
            nextBoard = updateBoard(nextBoard, rook, 1UL shl rookFrom)
            nextBoard = updateBoard(nextBoard, rook, 1UL shl rookTo)
        } else { // King Side Castling
            val rookFrom = if (piece.color == Color.White) 7 else 63
            val rookTo = if (piece.color == Color.White) 5 else 61
            val rook = Piece(PieceType.Rook, piece.color)
            nextBoard = updateBoard(nextBoard, rook, 1UL shl rookFrom)
            nextBoard = updateBoard(nextBoard, rook, 1UL shl rookTo)
        }
    }

    // updating enPassant square if any
    if (piece.type == PieceType.Pawn && abs(from - to) == 16) {
        val epSquare = if (to > from) from + 8 else from - 8
        nextBoard = nextBoard.copy(enPassantSquare = epSquare)
    }

    if (!updateHash) return nextBoard

    // Update Zobrist Hash and History
    val newHash = computeHash(nextBoard)
    val newHistory = if (piece.type == PieceType.Pawn || captured != null) {
        listOf(newHash)
    } else {
        board.history + newHash
    }

    return nextBoard.copy(zobristHash = newHash, history = newHistory)
}

fun updateBoard(board: Board, piece: Piece, mask: ULong): Board {
    return when (piece.color) {
        Color.White -> when (piece.type) {
            PieceType.Pawn -> board.copy(whitePawn = board.whitePawn xor mask)
            PieceType.Rook -> board.copy(whiteRook = board.whiteRook xor mask)
            PieceType.Knight -> board.copy(whiteKnight = board.whiteKnight xor mask)
            PieceType.Bishop -> board.copy(whiteBishop = board.whiteBishop xor mask)
            PieceType.Queen -> board.copy(whiteQueen = board.whiteQueen xor mask)
            PieceType.King -> board.copy(whiteKing = board.whiteKing xor mask)
        }

        Color.Black -> when (piece.type) {
            PieceType.Pawn -> board.copy(blackPawn = board.blackPawn xor mask)
            PieceType.Rook -> board.copy(blackRook = board.blackRook xor mask)
            PieceType.Knight -> board.copy(blackKnight = board.blackKnight xor mask)
            PieceType.Bishop -> board.copy(blackBishop = board.blackBishop xor mask)
            PieceType.Queen -> board.copy(blackQueen = board.blackQueen xor mask)
            PieceType.King -> board.copy(blackKing = board.blackKing xor mask)
        }
    }
}

fun updateGameState(board: Board, from: Int, to: Int, piece: Piece, captured: Piece?): Board {
    var wks = board.whiteKingSideCastle
    var wqs = board.whiteQueenSideCastle
    var bks = board.blackKingSideCastle
    var bqs = board.blackQueenSideCastle

    fun updateCastling(pos: Int) {
        when (pos) {
            0 -> wqs = false
            7 -> wks = false
            56 -> bqs = false
            63 -> bks = false
        }
    }

    if (piece.type == PieceType.King) {
        if (piece.color == Color.White) {
            wks = false
            wqs = false
        } else {
            bks = false
            bqs = false
        }
    }

    val hmc = if (piece.type == PieceType.Pawn || captured != null) 0 else board.halfMoveClock + 1

    updateCastling(from)
    updateCastling(to)

    return board.copy(
        isWhiteMove = !board.isWhiteMove,
        enPassantSquare = null,
        whiteKingSideCastle = wks,
        whiteQueenSideCastle = wqs,
        blackKingSideCastle = bks,
        blackQueenSideCastle = bqs,
        halfMoveClock = hmc
    )
}
