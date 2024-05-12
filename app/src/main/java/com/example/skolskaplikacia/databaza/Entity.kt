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


@Entity(tableName = "rozvrh")
data class Rozvrh(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rozvrhId: Int,
    val osobaId: Int,
    val den: Int,
    val blok: Int,
    val predmet: String,
    val ucebna: String
)

@Entity(tableName = "deti", foreignKeys = [
    ForeignKey(
        entity = Osoba::class,
        parentColumns = arrayOf("osobaId"),
        childColumns = arrayOf("rodic"),
        onDelete = ForeignKey.CASCADE
    )
])
data class Deti(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rodic: Int,
    val dietaId: Int,
    val meno: String,
    val priezvisko: String
)

@Entity(tableName = "spravy")
data class Spravy(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val osobaId: Int,
    val spravaId: Int,
    val datum: String,
    val cas: String,
    val sprava: String?,
)

@Entity(tableName = "predmety")
data class Predmety(
    @PrimaryKey(autoGenerate = true) val predmetId: Int = 0,
    val osobaId: Int,
    val predmet: String
)

@Entity(tableName = "kategorie", foreignKeys = [
    ForeignKey(
        entity = Predmety::class,
        parentColumns = arrayOf("predmetId"),
        childColumns = arrayOf("predmetId"),
        onDelete = ForeignKey.CASCADE
    )
])
data class Kategorie(
    @PrimaryKey(autoGenerate = true) val kategoriaId: Int,
    val predmetId: Int,
    val typ: String,
    val nazov: String,
    val vaha: String,
    val max_body: Double?,
)

@Entity(tableName = "znamkaNaPrepocet", foreignKeys = [
    ForeignKey(
        entity = Kategorie::class,
        parentColumns = arrayOf("kategoriaId"),
        childColumns = arrayOf("kategoriaId"),
        onDelete = ForeignKey.CASCADE
    )
])
data class ZnamkaNaPrepocet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val kategoriaId: Int,
    val rozsah_od: Int,
    val rozsah_do: Int,
    val znamka: Int
)

@Entity(tableName = "znamky", foreignKeys = [
    ForeignKey(
        entity = Kategorie::class,
        parentColumns = arrayOf("kategoriaId"),
        childColumns = arrayOf("kategoriaId"),
        onDelete = ForeignKey.CASCADE
    )
])
data class Znamky(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val kategoriaId: Int,
    val znamka: Int,
    val podpis: Int
)

@Entity(tableName = "dochadzka")
data class Dochadzka(
    @PrimaryKey val id_dochadza: Int,
    val osobaId: Int,
    val den: String,
    val block: Int,
    val typ: Int,
    val poznamka: Int,
    val ospravedlnene: Int
)

