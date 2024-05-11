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

    @Query("DELETE FROM predmety WHERE osobaId = :id")
    suspend fun deletePredmety(id: Int)

    @Query("DELETE FROM predmety")
    suspend fun deleteAllPredmety()

    @Query("SELECT kategoriaId FROM kategorie")
    suspend fun getAllKategoriaId(): List<Int>

    @Query("SELECT * FROM predmety where osobaId = :id ")
    suspend fun getAllPredmety(id: Int): List<Predmety>

    @Query("SELECT * FROM predmety where predmetId = :id ")
    suspend fun getPredmet(id: Int): List<Predmety>

    @Query("SELECT * FROM kategorie")
    suspend fun getAllKategorie(): List<Kategorie>

    @Query("SELECT * FROM znamky")
    suspend fun getAllZnamky(): List<Znamky>

    @Query("SELECT * FROM znamkaNaPrepocet where kategoriaId = :id and rozsah_od <= :znamka and :znamka <= rozsah_do")
    suspend fun getZnamkuZprepoctu(id: Int, znamka: Int): List<ZnamkaNaPrepocet>
    @Insert
    suspend fun VlozitNaPrepocet(znamkaNaPrepocet: ZnamkaNaPrepocet)
}
