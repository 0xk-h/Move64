package com.hunter.move64.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hunter.move64.data.local.dao.GameDao
import com.hunter.move64.data.model.Converters
import com.hunter.move64.data.model.GameEntity

@Database(entities = [GameEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "move64_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
