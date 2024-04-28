package com.example.skolskaplikacia.databaza

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Osoba::class, Rozvrh::class, Deti::class], version = 2, exportSchema = false)
abstract class AppDatabaza : RoomDatabase() {
    abstract fun osobaDao(): OsobaDao
    abstract fun rozvrhDao(): RozvrhDao
    abstract fun detiDao(): DetiDao

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
