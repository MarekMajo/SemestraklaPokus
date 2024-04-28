package com.example.skolskaplikacia.repository

import com.example.skolskaplikacia.databaza.Rozvrh
import com.example.skolskaplikacia.databaza.RozvrhDao

class RozvrhRepository(private val rozvrhDao: RozvrhDao) {
    suspend fun insertRozvrh(rozvrh: Rozvrh){
        rozvrhDao.insertRozvrh(rozvrh)
    }

    suspend fun deleteAllRozvrhy() {
        rozvrhDao.deleteAllRozvrhy()
    }

    suspend fun getAllRozvrh(): List<Rozvrh> {
        return rozvrhDao.getAllRozvrhy()
    }

    suspend fun getRozvrhOsoby(id: Int): List<Rozvrh> {
        return rozvrhDao.getRozvrhOsoby(id)
    }

    suspend fun deleteRozvrhByID(id: Int) {
        rozvrhDao.deleteRozvrhByID(id)
    }

    suspend fun updateRozvrhByID(blok: Int, den: Int, predmet: String, ucebna: String, id: Int) {
        rozvrhDao.updateRozvrhByID(blok, den, predmet, ucebna, id)
    }
}