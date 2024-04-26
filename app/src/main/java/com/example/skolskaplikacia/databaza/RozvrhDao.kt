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

    @Query("SELECT * FROM rozvrh where osobaId = :id")
    suspend fun getRozvrhOsoby(id: Int): List<Rozvrh>

    @Query("delete FROM rozvrh where rozvrhId = :id")
    suspend fun deleteRozvrhByID(id: Int)

    @Query("update rozvrh set blok = :blok, den = :den, predmet = :predmet, ucebna = :ucebna where rozvrhId = :id ")
    suspend fun updateRozvrhByID(blok: Int, den: Int, predmet: String, ucebna: String, id: Int)
}