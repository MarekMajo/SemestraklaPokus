package com.example.skolskaplikacia.repository

import com.example.skolskaplikacia.databaza.Osoba
import com.example.skolskaplikacia.databaza.OsobaDao

class OsobaRepository(private val osobaDao: OsobaDao) {
    suspend fun insertOsoba(osoba: Osoba) {
        osobaDao.insertOsoba(osoba)
    }

    suspend fun deleteOsoba(osoba: Osoba) {
        osobaDao.deleteOsoba(osoba)
    }

    suspend fun jePrihlaseny(): Osoba? {
        return osobaDao.getLatestUser()
    }
}
