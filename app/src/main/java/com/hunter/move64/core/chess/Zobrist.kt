package com.hunter.move64.core.chess

import kotlin.random.Random
object Zobrist {
    private val rng = Random(123456)

    // 12 pieces (6 white, 6 black) * 64 squares
    val pieceKeys = Array(12) { LongArray(64) { rng.nextLong() } }
    val sideKey = rng.nextLong()
    // 4 bits for castling: white king, white queen, black king, black queen
    val castlingKeys = LongArray(16) { rng.nextLong() }
    // En passant can happen on 8 files. 
    // We only hash it if a capture is actually possible next turn, but simpler to hash the file.
    // Index 0-7 for files A-H.
    val enPassantKeys = LongArray(8) { rng.nextLong() }
}

fun pieceIndex(type: PieceType, color: Color): Int {
    val base = when (type) {
        PieceType.Pawn -> 0
        PieceType.Knight -> 1
        PieceType.Bishop -> 2
        PieceType.Rook -> 3
        PieceType.Queen -> 4
        PieceType.King -> 5
    }
    return if (color == Color.White) base else base + 6
}

fun castleMask(
    whiteKingSide: Boolean,
    whiteQueenSide: Boolean,
    blackKingSide: Boolean,
    blackQueenSide: Boolean
): Int {
    var mask = 0
    if (whiteKingSide) mask = mask or 1
    if (whiteQueenSide) mask = mask or 2
    if (blackKingSide) mask = mask or 4
    if (blackQueenSide) mask = mask or 8
    return mask
}

fun computeHash(b: Board): Long {
    var h = 0L

    fun applyBB(bb: ULong, index: Int) {
        var bits = bb
        while (bits != 0UL) {
            val sq = bits.countTrailingZeroBits()
            h = h xor Zobrist.pieceKeys[index][sq]
            bits = bits and (bits - 1u)
        }
    }

    applyBB(b.whitePawn, 0)
    applyBB(b.whiteKnight, 1)
    applyBB(b.whiteBishop, 2)
    applyBB(b.whiteRook, 3)
    applyBB(b.whiteQueen, 4)
    applyBB(b.whiteKing, 5)

    applyBB(b.blackPawn, 6)
    applyBB(b.blackKnight, 7)
    applyBB(b.blackBishop, 8)
    applyBB(b.blackRook, 9)
    applyBB(b.blackQueen, 10)
    applyBB(b.blackKing, 11)

    if (b.isWhiteMove) h = h xor Zobrist.sideKey

    h = h xor Zobrist.castlingKeys[castleMask(
        b.whiteKingSideCastle,
        b.whiteQueenSideCastle,
        b.blackKingSideCastle,
        b.blackQueenSideCastle
    )]

    b.enPassantSquare?.let {
        h = h xor Zobrist.enPassantKeys[it % 8]
    }

    return h
}
