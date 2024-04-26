package com.example.skolskaplikacia.network

data class LoginData(val username: String, val password: String)
data class LoginResponse(val result: Int)

data class UserId(val id: Int)
data class UserName(val meno: String, val priezvisko: String)

data class GetRozvrh(val list: List<RozvrhTuple>)

data class RozvrhTuple(
    val rozvrhId: Int,
    val den: Int,
    val blok: Int,
    val predmet: String,
    val trieda: String
)

