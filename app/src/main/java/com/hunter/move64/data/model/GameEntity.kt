package com.hunter.move64.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class GameType {
    PvP,
    PvE
}

const val InitialFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0"

@Entity(tableName = "game")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fen: String = InitialFEN,
    val gameType: GameType,
    val lastPlayed: Long = System.currentTimeMillis()
)

class Converters {
    @TypeConverter
    fun fromGameType(type: GameType): String = type.name

    @TypeConverter
    fun toGameType(value: String): GameType = GameType.valueOf(value)
}