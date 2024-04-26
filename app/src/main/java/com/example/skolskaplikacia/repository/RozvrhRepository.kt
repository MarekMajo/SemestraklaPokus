package com.example.skolskaplikacia.repository

import com.example.skolskaplikacia.databaza.Rozvrh
import com.example.skolskaplikacia.databaza.RozvrhDao

class RozvrhRepository(private val rozvrhDao: RozvrhDao) {
    suspend fun insertRozvrh(rozvrh: Rozvrh){
        rozvrhDao.insertRozvrh(rozvrh)
    }

    suspend fun deleteOsoba(rozvrh: Rozvrh) {
        rozvrhDao.deleteRozvrh(rozvrh)
    }
}