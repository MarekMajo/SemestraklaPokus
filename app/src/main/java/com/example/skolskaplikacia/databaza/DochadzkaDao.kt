package com.example.skolskaplikacia.databaza

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DochadzkaDao {
    @Insert
    suspend fun insertDochadzka(dochadzka: Dochadzka)

    @Query("SELECT * FROM dochadzka where osobaId = :id")
    suspend fun getDochadzka(id: Int): List<Dochadzka>

    @Query("delete FROM dochadzka where osobaId = :id")
    suspend fun deleteAllDochadzka(id: Int)
}