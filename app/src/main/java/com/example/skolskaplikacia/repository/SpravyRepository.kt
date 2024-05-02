package com.example.skolskaplikacia.repository

import com.example.skolskaplikacia.databaza.Spravy
import com.example.skolskaplikacia.databaza.SpravyDao

class SpravyRepository(private val spravyDao: SpravyDao) {
    suspend fun insertOsoba(sprava: Spravy) {
        spravyDao.insertSprava(sprava)
    }
    suspend fun deleteSprava(id: Int) {
        spravyDao.deleteSprava(id)
    }
    suspend fun deleteAllSprava() {
        spravyDao.deleteAllSprava()
    }
    suspend fun getAllSpravy(): List<Spravy> {
        return spravyDao.getAllSpravy()
    }
    suspend fun getUserSpravy(id: Int): List<Spravy> {
        return spravyDao.getUserSpravy(id)
    }
}