package com.example.skolskaplikacia.databaza
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "osoba")
data class Osoba(
    @PrimaryKey val osobaId: Int,
    val meno: String?  = null,
    val priezvisko: String?  = null
)
