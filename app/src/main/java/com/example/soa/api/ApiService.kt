package com.example.soa.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

//각 API 서비스를 제공하는 객체

interface ApiService {
    //notation으로 정의된 거 작성(회원가입은 POST)

    @POST("api/auth/signup")
    fun signUP(@Body req: SignUpRequest): Call<SignUpResponse>

 //   @POST("api/auth/upload")

    @POST("api/auth/login")
    fun login(@Body req: LoginRequest): Call<LoginResponse>

    @GET("api/auth/check")
    fun idcheck(@Query("appId") id: String): Call<IdCheckResponse>

}