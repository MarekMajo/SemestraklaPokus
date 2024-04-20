package com.example.skolskaplikacia.network

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("Mobilelogin")
    suspend fun loginUser(@Body loginData: LoginData): LoginResponse
}
