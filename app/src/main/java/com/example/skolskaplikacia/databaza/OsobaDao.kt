package com.example.skolskaplikacia.databaza
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OsobaDao {
    @Insert
    suspend fun insertOsoba(osoba: Osoba)

    @Delete
    suspend fun deleteOsoba(osoba: Osoba)

    @Query("SELECT * FROM osoba")
    suspend fun getAllOsoby(): List<Osoba>

    @Query("SELECT * FROM osoba ORDER BY osobaId DESC LIMIT 1")
    suspend fun getLatestUser(): Osoba?
}
