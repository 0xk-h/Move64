package com.hunter.move64.domain.service

import com.hunter.move64.core.chess.Board
import com.hunter.move64.core.chess.getInitialBoard
import com.hunter.move64.core.chess.parseFEN
import com.hunter.move64.core.chess.toFEN
import com.hunter.move64.data.model.GameType
import com.hunter.move64.data.repository.GameRepository

class GameService(
    private val gameRepo: GameRepository,
    private var id: Int? = null
) {
    suspend fun loadGame (type: GameType): Board {
        val game = gameRepo.loadGame(type)
        game?.let { id = it.id }
        return game?.let {
            parseFEN(it.fen)
        } ?: getInitialBoard()
    }

    suspend fun saveGame (type: GameType, board: Board) {
        val fen = toFEN(board)
        id = gameRepo.saveGame(id, type, fen)
    }
}