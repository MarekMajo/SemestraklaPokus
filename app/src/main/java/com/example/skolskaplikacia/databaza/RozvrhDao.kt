package com.example.skolskaplikacia.databaza

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RozvrhDao {
    @Insert
    suspend fun insertRozvrh(rozvrh: Rozvrh)

    @Delete
    suspend fun deleteRozvrh(rozvrh: Rozvrh)

    @Query("SELECT * FROM rozvrh")
    suspend fun getAllRozvrhy(): List<Rozvrh>
}