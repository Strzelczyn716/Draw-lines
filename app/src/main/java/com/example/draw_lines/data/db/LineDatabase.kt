package com.example.draw_lines.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.draw_lines.data.model.Line

@Database(
    entities = [Line::class],
    version = 1,
    exportSchema = false
)
abstract class LineDatabase : RoomDatabase() {

    abstract fun lineDao(): LineDao

    companion object {
        private const val DB_NAME: String = "shopping_database"

        @Volatile
        private var INSTANCE: LineDatabase? = null
        fun getDatabase(context: Context): LineDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LineDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
