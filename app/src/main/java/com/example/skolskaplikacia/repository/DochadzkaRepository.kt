package com.example.skolskaplikacia.repository

import com.example.skolskaplikacia.databaza.Dochadzka
import com.example.skolskaplikacia.databaza.DochadzkaDao

class DochadzkaRepository(private val dochadzkaDao: DochadzkaDao) {

    suspend fun insertdochadzka(dochadzka: Dochadzka) {
        dochadzkaDao.insertDochadzka(dochadzka)
    }

    suspend fun getDochadzka(id: Int): List <Dochadzka> {
        return dochadzkaDao.getDochadzka(id)
    }

    suspend fun deleteAllDochadzka(id: Int) {
        dochadzkaDao.deleteAllDochadzka(id)
    }

}