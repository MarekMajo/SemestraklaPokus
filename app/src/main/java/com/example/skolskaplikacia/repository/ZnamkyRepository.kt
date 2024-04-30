package com.example.skolskaplikacia.repository

import com.example.skolskaplikacia.databaza.Kategorie
import com.example.skolskaplikacia.databaza.Predmety
import com.example.skolskaplikacia.databaza.Znamky
import com.example.skolskaplikacia.databaza.ZnamkyDao

class ZnamkyRepository(private val znamkyDao: ZnamkyDao) {
    suspend fun vlozitPredmet(predmet: Predmety): Long {
        return znamkyDao.vlozitPredmet(predmet)
    }

    suspend fun vlozitKategoriu(kategorie: Kategorie): Long {
        return znamkyDao.vlozitKategoriu(kategorie)
    }

    suspend fun vlozitZnamka(znamka: Znamky) {
        znamkyDao.vlozitZnamka(znamka)
    }

    suspend fun deletePredmety(id: Int) {
        znamkyDao.deletePredmety(id)
    }

    suspend fun deleteAllPredmety() {
        znamkyDao.deleteAllPredmety()
    }
}