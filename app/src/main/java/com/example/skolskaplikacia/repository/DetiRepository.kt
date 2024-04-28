package com.example.skolskaplikacia.repository

import com.example.skolskaplikacia.databaza.Deti
import com.example.skolskaplikacia.databaza.DetiDao

class DetiRepository(private val detiDao: DetiDao) {

    suspend fun insertDieta(dieta: Deti) {
        detiDao.insertDieta(dieta)
    }

    suspend fun getAllDeti(): List <Deti> {
        return detiDao.getAllDeti()
    }

}