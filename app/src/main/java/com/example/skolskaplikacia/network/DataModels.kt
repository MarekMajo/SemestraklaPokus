package com.example.skolskaplikacia.network

import java.time.DateTimeException

data class LoginData(val username: String, val password: String)
data class LoginResponse(val result: Int)

data class UserId(val id: Int)
data class UserName(val meno: String, val priezvisko: String)

data class GetRozvrh(val list: List<RozvrhTuple>)

data class GetDeti(val list: List<DetiTuple>)

data class GetSpravy(val list: List<SpravyTuple>)

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

