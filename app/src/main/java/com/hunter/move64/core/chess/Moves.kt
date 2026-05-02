package com.hunter.move64.core.chess

val notAFile = 0xfefefefefefefefeUL
val notHFile = 0x7f7f7f7f7f7f7f7fUL
val notABFile = 0xfcfcfcfcfcfcfcfcUL
val notGHFile = 0x3f3f3f3f3f3f3f3fUL

fun knightMoves(index: Int): ULong {
    val bit = 1UL shl index

    var res = 0UL

    res = res or ((bit shl 17) and notAFile)
    res = res or ((bit shl 15) and notHFile)
    res = res or ((bit shl 10) and notABFile)
    res = res or ((bit shl 6)  and notGHFile)

    res = res or ((bit shr 17) and notHFile)
    res = res or ((bit shr 15) and notAFile)
    res = res or ((bit shr 10) and notGHFile)
    res = res or ((bit shr 6)  and notABFile)

    return res
}

fun kingMoves(index: Int): ULong {
    val bit = 1UL shl index

    var res = 0UL

    // left and right
    res = res or ((bit shl 1) and notAFile)
    res = res or ((bit shr 1) and notHFile)

    // up and down
    res = res or (bit shl 8)
    res = res or (bit shr 8)

    // diagonals
    res = res or ((bit shl 9) and notAFile)
    res = res or ((bit shl 7) and notHFile)
    res = res or ((bit shr 7) and notAFile)
    res = res or ((bit shr 9) and notHFile)

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

fun whitePawnAttackersTo(index: Int): ULong {
    val bit = 1UL shl index
    var res = 0UL

    res = res or ((bit shr 7) and notHFile)
    res = res or ((bit shr 9) and notAFile)

    return res
}

fun blackPawnAttackersTo(index: Int): ULong {
    val bit = 1UL shl index
    var res = 0UL

    res = res or ((bit shl 7) and notAFile)
    res = res or ((bit shl 9) and notHFile)

    return res
}
