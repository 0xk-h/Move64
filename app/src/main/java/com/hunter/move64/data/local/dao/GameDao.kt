package com.hunter.move64.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hunter.move64.data.model.GameEntity
import com.hunter.move64.data.model.GameType
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM game ORDER BY lastPlayed DESC")
    fun getAllGames(): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity): Long

    @Delete
    suspend fun deleteGame(game: GameEntity)

    @Query("DELETE FROM game WHERE gameType = :type")
    suspend fun deleteByType(type: GameType)

    @Query("SELECT * FROM game WHERE gameType = :type ORDER BY lastPlayed DESC LIMIT 1")
    suspend fun getLastPlayedGameByType(type: GameType): GameEntity?

    @Query("SELECT * FROM game WHERE id = :id")
    suspend fun getGameById(id: Int): GameEntity?
}
