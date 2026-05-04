package com.hunter.move64.core.chess

enum class Pieces {
    WhiteKing,
    WhiteQueen,
    WhiteBishop,
    WhiteKnight,
    WhiteRook,
    WhitePawn,
    BlackKing,
    BlackQueen,
    BlackBishop,
    BlackKnight,
    BlackRook,
    BlackPawn;

    val type: PieceType
        get() = when (this) {
            WhiteKing, BlackKing -> PieceType.King
            WhiteQueen, BlackQueen -> PieceType.Queen
            WhiteBishop, BlackBishop -> PieceType.Bishop
            WhiteKnight, BlackKnight -> PieceType.Knight
            WhiteRook, BlackRook -> PieceType.Rook
            WhitePawn, BlackPawn -> PieceType.Pawn
        }

    val color: Color
        get() = when (this) {
            WhiteKing, WhiteQueen, WhiteBishop, WhiteKnight, WhiteRook, WhitePawn -> Color.White
            BlackKing, BlackQueen, BlackBishop, BlackKnight, BlackRook, BlackPawn -> Color.Black
        }
}

enum class PieceType {
    King,
    Queen,
    Bishop,
    Knight,
    Rook,
    Pawn
}

enum class Color {
    White,
    Black;

    val opposite: Color
        get() = if (this == White) Black else White
}

data class Piece(
    val type: PieceType,
    val color: Color
)