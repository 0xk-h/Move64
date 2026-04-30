package com.hunter.move64.core.chess

data class Board (
    // BitBoards
    val whitePawn: Long = 0L,
    val whiteRook: Long = 0L,
    val whiteKnight: Long = 0L,
    val whiteBishop: Long = 0L,
    val whiteQueen: Long = 0L,
    val whiteKing: Long = 0L,

    val blackPawn: Long = 0L,
    val blackRook: Long = 0L,
    val blackKnight: Long = 0L,
    val blackBishop: Long = 0L,
    val blackQueen: Long = 0L,
    val blackKing: Long = 0L,

    // Game State
    val isWhiteMove: Boolean = true,

    // Special Rules
    val whiteKingSideCastle: Boolean = true,
    val whiteQueenSideCastle: Boolean = true,
    val blackKingSideCastle: Boolean = true,
    val blackQueenSideCastle: Boolean = true,

    val enPassantSquare: Int? = null,

    // Draw Condition
    val halfMoveClock: Int = 0
) {
    val whitePieces: Long
        get() = whitePawn or whiteRook or whiteBishop or whiteKnight or whiteQueen or whiteKing

    val blackPieces: Long
        get() = blackPawn or blackKing or blackBishop or blackQueen or blackKnight or blackRook

    val occupied: Long
        get() = whitePieces or blackPieces

    val empty: Long
        get() = occupied.inv()

    fun toGrid(): List<Pieces?> {
        val grid = MutableList<Pieces?>(64) { null }

        fun setPieces(bb: Long, piece: Pieces) {
            var bb = bb
            while (bb != 0L) {
                val index = java.lang.Long.numberOfTrailingZeros(bb)
                grid[index] = piece
                bb = bb and (bb - 1)
            }
        }

        // White
        setPieces(whitePawn,   Pieces.WhitePawn)
        setPieces(whiteRook,   Pieces.WhiteRook)
        setPieces(whiteKnight, Pieces.WhiteKnight)
        setPieces(whiteBishop, Pieces.WhiteBishop)
        setPieces(whiteQueen,  Pieces.WhiteQueen)
        setPieces(whiteKing,   Pieces.WhiteKing)

        // Black
        setPieces(blackPawn,   Pieces.BlackPawn)
        setPieces(blackRook,   Pieces.BlackRook)
        setPieces(blackKnight, Pieces.BlackKnight)
        setPieces(blackBishop, Pieces.BlackBishop)
        setPieces(blackQueen,  Pieces.BlackQueen)
        setPieces(blackKing,   Pieces.BlackKing)

        return grid
    }
}

fun getInitialBoard(): Board {
    return Board(
        // White pieces
        whitePawn   = 0x000000000000FF00L,
        whiteRook   = 0x0000000000000081L,
        whiteKnight = 0x0000000000000042L,
        whiteBishop = 0x0000000000000024L,
        whiteQueen  = 0x0000000000000008L,
        whiteKing   = 0x0000000000000010L,

        // Black pieces
        blackPawn   = 0x00FF000000000000L,
        blackRook   = 0x8100000000000000UL.toLong(),
        blackKnight = 0x4200000000000000L,
        blackBishop = 0x2400000000000000L,
        blackQueen  = 0x0800000000000000L,
        blackKing   = 0x1000000000000000L,

        // Game state
        isWhiteMove = true,

        // Castling rights
        whiteKingSideCastle = true,
        whiteQueenSideCastle = true,
        blackKingSideCastle = true,
        blackQueenSideCastle = true,

        enPassantSquare = null,
        halfMoveClock = 0
    )
}