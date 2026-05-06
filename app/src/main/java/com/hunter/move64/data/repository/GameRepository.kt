package com.hunter.move64.data.repository

import com.hunter.move64.data.local.dao.GameDao
import com.hunter.move64.data.model.GameEntity
import com.hunter.move64.data.model.GameType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameRepository(private val gameDao: GameDao) {
    val allGames: Flow<List<GameEntity>> = gameDao.getAllGames()

    suspend fun insert(game: GameEntity): Long {
        return gameDao.insertGame(game)
    }

    suspend fun delete(game: GameEntity) {
        gameDao.deleteGame(game)
    }

    suspend fun deleteByType(gameType: GameType) {
        gameDao.deleteByType(gameType)
    }

    suspend fun getGameById(id: Int): GameEntity? {
        return gameDao.getGameById(id)
    }

    suspend fun saveGame(id: Int?, type: GameType, fen: String): Int {
        val newID = if (id == null) {
            insert(
                GameEntity(
                    fen = fen,
                    gameType = type
                )
            )
        } else {
            insert(
                GameEntity(
                    id = id,
                    fen = fen,
                    gameType = type
                )
            )
        }

        return newID.toInt()
    }

    suspend fun loadGame(type: GameType): GameEntity? {
        return gameDao.getLastPlayedGameByType(type)
    }
}
