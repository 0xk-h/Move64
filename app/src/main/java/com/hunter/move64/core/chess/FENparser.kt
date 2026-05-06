package com.hunter.move64.core.chess

fun toFEN(board: Board): String {
    val fen = StringBuilder()

    // 1. Piece placement
    for (rank in 7 downTo 0) {
        var emptyCount = 0
        for (file in 0..7) {
            val index = rank * 8 + file
            val piece = board.getPiece(index)

            if (piece == null) {
                emptyCount++
            } else {
                if (emptyCount > 0) {
                    fen.append(emptyCount)
                    emptyCount = 0
                }
                fen.append(pieceToChar(piece))
            }
        }
        if (emptyCount > 0) {
            fen.append(emptyCount)
        }
        if (rank > 0) {
            fen.append('/')
        }
    }

    fen.append(' ')

    // 2. Side to move
    fen.append(if (board.isWhiteMove) 'w' else 'b')

    fen.append(' ')

    // 3. Castling rights
    var castling = ""
    if (board.whiteKingSideCastle) castling += 'K'
    if (board.whiteQueenSideCastle) castling += 'Q'
    if (board.blackKingSideCastle) castling += 'k'
    if (board.blackQueenSideCastle) castling += 'q'
    
    if (castling.isEmpty()) {
        fen.append('-')
    } else {
        fen.append(castling)
    }

    fen.append(' ')

    // 4. En passant target square
    if (board.enPassantSquare == null) {
        fen.append('-')
    } else {
        fen.append(indexToAlgebraic(board.enPassantSquare))
    }

    fen.append(' ')

    // 5. Halfmove clock
    fen.append(board.halfMoveClock)

    fen.append(' ')

    // 6. Fullmove number
    // Standard FEN starts at 1. If board.fullMoveCount starts at 0, we might want to adjust.
    // However, we'll use board.fullMoveCount as it is stored.
    fen.append(board.fullMoveCount)

    return fen.toString()
}

fun parseFEN(fen: String): Board {
    val parts = fen.split(" ")
    if (parts.size < 4) throw IllegalArgumentException("Invalid FEN: $fen")

    val piecePlacement = parts[0]
    val sideToMove = parts[1]
    val castlingRights = parts[2]
    val enPassantTarget = parts[3]
    val halfMoveClock = if (parts.size > 4) parts[4].toInt() else 0
    val fullMoveCount = if (parts.size > 5) parts[5].toInt() else 1

    var whitePawn = 0UL
    var whiteRook = 0UL
    var whiteKnight = 0UL
    var whiteBishop = 0UL
    var whiteQueen = 0UL
    var whiteKing = 0UL
    var blackPawn = 0UL
    var blackRook = 0UL
    var blackKnight = 0UL
    var blackBishop = 0UL
    var blackQueen = 0UL
    var blackKing = 0UL

    val ranks = piecePlacement.split("/")
    for (r in 0..7) {
        val rankStr = ranks[r]
        val rank = 7 - r
        var file = 0
        for (char in rankStr) {
            if (char.isDigit()) {
                file += char.toString().toInt()
            } else {
                val index = rank * 8 + file
                val mask = 1UL shl index
                when (char) {
                    'P' -> whitePawn = whitePawn or mask
                    'R' -> whiteRook = whiteRook or mask
                    'N' -> whiteKnight = whiteKnight or mask
                    'B' -> whiteBishop = whiteBishop or mask
                    'Q' -> whiteQueen = whiteQueen or mask
                    'K' -> whiteKing = whiteKing or mask
                    'p' -> blackPawn = blackPawn or mask
                    'r' -> blackRook = blackRook or mask
                    'n' -> blackKnight = blackKnight or mask
                    'b' -> blackBishop = blackBishop or mask
                    'q' -> blackQueen = blackQueen or mask
                    'k' -> blackKing = blackKing or mask
                }
                file++
            }
        }
    }

    val isWhiteMove = sideToMove == "w"

    val whiteKingSideCastle = castlingRights.contains('K')
    val whiteQueenSideCastle = castlingRights.contains('Q')
    val blackKingSideCastle = castlingRights.contains('k')
    val blackQueenSideCastle = castlingRights.contains('q')

    val enPassantSquare = if (enPassantTarget == "-") null else algebraicToIndex(enPassantTarget)

    return Board(
        whitePawn = whitePawn,
        whiteRook = whiteRook,
        whiteKnight = whiteKnight,
        whiteBishop = whiteBishop,
        whiteQueen = whiteQueen,
        whiteKing = whiteKing,
        blackPawn = blackPawn,
        blackRook = blackRook,
        blackKnight = blackKnight,
        blackBishop = blackBishop,
        blackQueen = blackQueen,
        blackKing = blackKing,
        whiteKingSideCastle = whiteKingSideCastle,
        whiteQueenSideCastle = whiteQueenSideCastle,
        blackKingSideCastle = blackKingSideCastle,
        blackQueenSideCastle = blackQueenSideCastle,
        enPassantSquare = enPassantSquare,
        halfMoveClock = halfMoveClock,
        isWhiteMove = isWhiteMove,
        fullMoveCount = fullMoveCount
    ).let {
        val hash = computeHash(it)
        it.copy(zobristHash = hash, history = listOf(hash))
    }
}

private fun pieceToChar(piece: Piece): Char {
    val char = when (piece.type) {
        PieceType.Pawn -> 'P'
        PieceType.Rook -> 'R'
        PieceType.Knight -> 'N'
        PieceType.Bishop -> 'B'
        PieceType.Queen -> 'Q'
        PieceType.King -> 'K'
    }
    return if (piece.color == Color.White) char else char.lowercaseChar()
}

private fun indexToAlgebraic(index: Int): String {
    val file = index % 8
    val rank = index / 8
    val fileName = ('a'.code + file).toChar()
    val rankName = (rank + 1).toString()
    return "$fileName$rankName"
}

private fun algebraicToIndex(algebraic: String): Int {
    val file = algebraic[0] - 'a'
    val rank = algebraic[1].toString().toInt() - 1
    return rank * 8 + file
}
