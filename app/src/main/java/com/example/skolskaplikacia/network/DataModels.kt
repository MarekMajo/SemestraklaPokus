package com.example.skolskaplikacia.network

data class LoginData(val username: String, val password: String)
data class LoginResponse(val result: Int)

data class UserId(val id: Int)
data class UserName(val meno: String, val priezvisko: String)

data class GetRozvrh(val list: List<RozvrhTuple>)

data class GetDeti(val list: List<DetiTuple>)

data class GetSpravy(val list: List<SpravyTuple>)

data class GetZnamky(val list: List<ZnamkyTuple>)

data class KategorieAllid(val list: List<kategorieZnamky>)

data class PrazdnyReturn(val id: Int)

data class PoziadavkyNaPodpisZnamok(val kategorieAllid: List<Int>, val userId: Int)

data class kategorieZnamky(
    val kategoria_id: Int,
    val znamka: Int,
    val znamka_od: Int,
    val znamka_do: Int
)

data class RozvrhTuple(
    val rozvrhId: Int,
    val osobaId: Int,
    val den: Int,
    val blok: Int,
    val predmet: String,
    val trieda: String
)

data class DetiTuple(
    val dietaId: Int,
    val meno: String,
    val priezvisko: String
)

data class SpravyTuple(
    val osobaId: Int,
    val spravaId: Int,
    val sprava: String?,
    val datum: String,
    val cas: String
)

data class ZnamkyTuple(
    val predmet: String,
    val kategorie: List<KategorieTuple>
)
data class KategorieTuple(
    val kategoriaNazov: String,
    val kategoria_id: Int,
    val typ_znamky: String,
    val vaha: String,
    val max_body: Double?,
    val znamkyPodpis: List<Znamka>,
    val znamkyNePodpis: List<Znamka>
)
data class Znamka(
    val znamka: Int,
    val znamkaId: Int
)
