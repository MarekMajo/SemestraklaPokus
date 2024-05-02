package com.example.skolskaplikacia.databaza

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface SpravyDao {
    @Insert
    suspend fun insertSprava(sprava: Spravy)

    @Query("delete from spravy where spravaId = :id")
    suspend fun deleteSprava(id: Int)

    @Query("delete from spravy")
    suspend fun deleteAllSprava()

    @Query("SELECT * FROM spravy")
    suspend fun getAllSpravy(): List<Spravy>

    @Query("SELECT * FROM spravy where osobaId = :id")
    suspend fun getUserSpravy(id: Int): List<Spravy>
}