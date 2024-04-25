package com.example.skolskaplikacia.network

data class LoginData(val username: String, val password: String)
data class LoginResponse(val result: Int)

data class UserId(val id: Int)
data class UserName(val meno: String, val priezvisko: String)
