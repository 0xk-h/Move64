package com.hunter.move64.core.chess

import kotlin.math.abs

fun applyMove(board: Board, from: Int, to: Int, promotion: PieceType?): Board {
    var board = board
    val piece = board.getPiece(from)
    val captured = board.getPiece(to)

    println(piece!!.type)
    println("from: $from ; to: $to")



    // just some customs
    board = updateGameState(board, from, piece!!)

    // remove from previous position
    board = updateBoard(board, piece, 1UL shl from)

    // add it in the new position
    board = if (promotion != null) {
        updateBoard(board, Piece(promotion, piece.color), 1UL shl to)
    } else {
        updateBoard(board, piece, 1UL shl to)
    }

    // if castling happens should move the rook
    if (piece.type == PieceType.King && abs(from - to) == 2) {
        // Queen Side Castling
        if (to < from) {
            // from Position of rook
            println("from position of rook: ${to - 2}")
            println("to position of rook: ${to + 1}")
            board = updateBoard(board, Piece(PieceType.Rook, piece.color), 1UL shl (to - 2))
            // to Position of rook
            board = updateBoard(board, Piece(PieceType.Rook, piece.color), 1UL shl (to + 1))
        }
        // King Side Castling
        else {
            // from Position of rook
            println("from position of rook: ${to + 1}")
            println("to position of rook: ${to - 1}")
            board = updateBoard(board, Piece(PieceType.Rook, piece.color), 1UL shl (to + 1))
            // to Position of rook
            board = updateBoard(board, Piece(PieceType.Rook, piece.color), 1UL shl (to - 1))
        }
    }

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

fun updateGameState(board: Board, from: Int, piece: Piece): Board {
    when (piece.type) {
        PieceType.King -> {
            if (piece.color == Color.White) {
                board.whiteKingSideCastle = false
                board.whiteQueenSideCastle = false
            } else {
                board.blackKingSideCastle = false
                board.blackQueenSideCastle = false
            }
        }
        PieceType.Rook -> {
            when (from) {
                0 -> board.whiteQueenSideCastle = false
                7 -> board.whiteKingSideCastle = false
                56 -> board.blackQueenSideCastle = false
                63 -> board.blackKingSideCastle = false
                else -> {}
            }
        }
        else -> {}
    }

    board.isWhiteMove = !board.isWhiteMove
    board.enPassantSquare = null

    return board
}