package com.example.skolskaplikacia.network

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("Mobilelogin")
    suspend fun loginUser(@Body loginData: LoginData): LoginResponse

    @POST("MobileGetMeno")
    suspend fun getUserName(@Body userId: UserId): UserName

    @POST("MobileGetRozvrh")
    suspend fun getUserRozvrh(@Body userId: UserId): GetRozvrh

    @POST("MobileGetDeti")
    suspend fun getUserDeti(@Body userId: UserId): GetDeti

    @POST("MobileGetSpravy")
    suspend fun MobileGetSpravy(@Body userId: UserId): GetSpravy
}
