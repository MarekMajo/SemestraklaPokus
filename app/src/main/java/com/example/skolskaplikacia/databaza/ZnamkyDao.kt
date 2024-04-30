package com.example.skolskaplikacia.databaza

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ZnamkyDao {
    @Insert
    suspend fun vlozitPredmet(predmet: Predmety): Long

    @Insert
    suspend fun vlozitKategoriu(kategorie: Kategorie): Long

    @Insert
    suspend fun vlozitZnamka(znamka: Znamky)

    @Query ("delete from predmety where osobaId = :id")
    suspend fun deletePredmety(id: Int)

    @Query ("delete from predmety")
    suspend fun deleteAllPredmety()
}