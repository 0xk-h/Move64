package com.hunter.move64.core.chess

data class Board (
    // BitBoards
    val whitePawn: ULong = 0UL,
    val whiteRook: ULong = 0UL,
    val whiteKnight: ULong = 0UL,
    val whiteBishop: ULong = 0UL,
    val whiteQueen: ULong = 0UL,
    val whiteKing: ULong = 0UL,

    val blackPawn: ULong = 0UL,
    val blackRook: ULong = 0UL,
    val blackKnight: ULong = 0UL,
    val blackBishop: ULong = 0UL,
    val blackQueen: ULong = 0UL,
    val blackKing: ULong = 0UL,

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
    val whitePieces: ULong
        get() = whitePawn or whiteRook or whiteBishop or whiteKnight or whiteQueen or whiteKing

    val blackPieces: ULong
        get() = blackPawn or blackKing or blackBishop or blackQueen or blackKnight or blackRook

    val occupied: ULong
        get() = whitePieces or blackPieces

    val empty: ULong
        get() = occupied.inv()

    fun isAttacked(index: Int, color: Color): Boolean {
        // color is the inverse of the enemy color (color of ally)
        val occupied = occupied

        val diagonal = bishopMoves(index, occupied)
        var enemies = if(color == Color.White) blackQueen or blackBishop else whiteQueen or whiteBishop
        if (diagonal and enemies != 0UL) return true

        val straight = rookMoves(index, occupied)
        enemies = if(color == Color.White) blackQueen or blackRook else whiteQueen or whiteRook
        if (straight and enemies != 0UL) return true

        val knight = knightMoves(index)
        enemies = if(color == Color.White) blackKnight else whiteKnight
        if (knight and enemies != 0UL) return true

        val king = kingMoves(index)
        enemies = if(color == Color.White) blackKing else whiteKing
        if (king and enemies != 0UL) return true

        val pawn = if(color == Color.White) blackPawnAttackersTo(index) else whitePawnAttackersTo(index)
        enemies = if(color == Color.White) blackPawn else whitePawn
        if (pawn and enemies != 0UL) return true

        return false
    }

    fun getPiece(index: Int): Piece? {
        val mask = 1UL shl index

        // White pieces
        if ((whitePawn and mask) != 0UL) return Piece(PieceType.Pawn, Color.White)
        if ((whiteKnight and mask) != 0UL) return Piece(PieceType.Knight, Color.White)
        if ((whiteBishop and mask) != 0UL) return Piece(PieceType.Bishop, Color.White)
        if ((whiteRook and mask) != 0UL) return Piece(PieceType.Rook, Color.White)
        if ((whiteQueen and mask) != 0UL) return Piece(PieceType.Queen, Color.White)
        if ((whiteKing and mask) != 0UL) return Piece(PieceType.King, Color.White)

        // Black pieces
        if ((blackPawn and mask) != 0UL) return Piece(PieceType.Pawn, Color.Black)
        if ((blackKnight and mask) != 0UL) return Piece(PieceType.Knight, Color.Black)
        if ((blackBishop and mask) != 0UL) return Piece(PieceType.Bishop, Color.Black)
        if ((blackRook and mask) != 0UL) return Piece(PieceType.Rook, Color.Black)
        if ((blackQueen and mask) != 0UL) return Piece(PieceType.Queen, Color.Black)
        if ((blackKing and mask) != 0UL) return Piece(PieceType.King, Color.Black)

        return null
    }

    fun getCurrPlayer(): Color {
        return if (isWhiteMove) Color.White else Color.Black
    }

    fun toGrid(): List<Pieces?> {
        val grid = MutableList<Pieces?>(64) { null }

        fun setPieces(bb: ULong, piece: Pieces) {
            var currentBb = bb
            while (currentBb != 0UL) {
                val index = java.lang.Long.numberOfTrailingZeros(currentBb.toLong())
                grid[index] = piece
                currentBb = currentBb and (currentBb - 1u)
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
        whitePawn   = 0x000000000000FF00UL,
        whiteRook   = 0x0000000000000081UL,
        whiteKnight = 0x0000000000000042UL,
        whiteBishop = 0x0000000000000024UL,
        whiteQueen  = 0x0000000000000008UL,
        whiteKing   = 0x0000000000000010UL,

        // Black pieces
        blackPawn   = 0x00FF000000000000UL,
        blackRook   = 0x8100000000000000UL,
        blackKnight = 0x4200000000000000UL,
        blackBishop = 0x2400000000000000UL,
        blackQueen  = 0x0800000000000000UL,
        blackKing   = 0x1000000000000000UL,

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