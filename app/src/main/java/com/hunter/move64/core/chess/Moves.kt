package com.hunter.move64.core.chess

fun knightMoves(index: Int): ULong {
    val moves = listOf(17, 15, 10, 6, -6, -10, -15, -17)

    var res = 0UL

    for (offset in moves) {
        val target = index + offset
        if (target in 0..63) {
            res = res or (1UL shl target)
        }
    }

    return res
}

fun kingMoves(index: Int): ULong {
    val moves = listOf(1, 7, 8, 9, -1, -7, -8, -9)

    var res = 0UL

    for (offset in moves) {
        val target = index + offset
        if (target in 0..63) {
            res = res or (1UL shl target)
        }
    }

    return res
}

fun bishopMoves(index: Int, occupied: ULong): ULong {
    val directions = listOf(9, 7, -7, -9)

    var res = 0UL

    for (dir in directions) {
        var curr = index

        while (true) {
            val next = curr + dir
            if (next !in 0..63) break

            // cases like 8 rank then 1 rank
            if (kotlin.math.abs((curr % 8) - (next % 8)) != 1) break

            val bit = 1UL shl next
            res = res or bit
            curr = next

            if (occupied and bit != 0UL) break
        }
    }

    return res
}

fun rookMoves(index: Int, occupied: ULong): ULong {
    var res = 0UL

    // up
    var s = index
    while (s < 56) {
        s += 8
        val bit = 1UL shl s
        res = res or bit
        if (occupied and bit != 0UL) break
    }

    // down
    s = index
    while (s >= 8) {
        s -= 8
        val bit = 1UL shl s
        res = res or bit
        if (occupied and bit != 0UL) break
    }

    // right
    s = index
    while (s % 8 != 7) {
        s += 1
        val bit = 1UL shl s
        res = res or bit
        if (occupied and bit != 0UL) break
    }

    // left
    s = index
    while (s % 8 != 0) {
        s -= 1
        val bit = 1UL shl s
        res = res or bit
        if (occupied and bit != 0UL) break
    }

    return res
}

fun queenMoves(index: Int, occupied: ULong): ULong {
    return rookMoves(index, occupied) or bishopMoves(index, occupied)
}
