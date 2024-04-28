package com.example.skolskaplikacia.network

data class LoginData(val username: String, val password: String)
data class LoginResponse(val result: Int)

data class UserId(val id: Int)
data class UserName(val meno: String, val priezvisko: String)

data class GetRozvrh(val list: List<RozvrhTuple>)

data class GetDeti(val list: List<DetiTuple>)

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

