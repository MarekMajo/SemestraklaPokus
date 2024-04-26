package com.example.skolskaplikacia.databaza
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "osoba")
data class Osoba(
    @PrimaryKey val osobaId: Int,
    val meno: String? = null,
    val priezvisko: String? = null
)

@Entity(tableName = "rozvrh", foreignKeys = [
    ForeignKey(
        entity = Osoba::class,
        parentColumns = arrayOf("osobaId"),
        childColumns = arrayOf("osobaId"),
        onDelete = ForeignKey.CASCADE
    )
])
data class Rozvrh(
    @PrimaryKey(autoGenerate = true) val rozvrhId: Int,
    val osobaId: Int,
    val den: Int,
    val blok: Int,
    val predmet: String,
    val ucebna: String
)
