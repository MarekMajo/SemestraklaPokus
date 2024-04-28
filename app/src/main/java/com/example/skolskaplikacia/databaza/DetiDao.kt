package com.example.skolskaplikacia.databaza

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DetiDao {
    @Insert
    suspend fun insertDieta(dieta: Deti)

    @Query("SELECT * FROM deti")
    suspend fun getAllDeti(): List<Deti>
}