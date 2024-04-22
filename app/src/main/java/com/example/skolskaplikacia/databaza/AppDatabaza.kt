package com.example.skolskaplikacia.databaza

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Osoba::class], version = 1, exportSchema = false)
abstract class AppDatabaza : RoomDatabase() {
    abstract fun osobaDao(): OsobaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabaza? = null

        fun getDatabase(context: Context): AppDatabaza {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabaza::class.java,
                    "udaje"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
