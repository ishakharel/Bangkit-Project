package com.ecoloops.ecoloopsapp.data.remote.retrofit

import com.ecoloops.ecoloopsapp.data.model.LoginRequest
import com.ecoloops.ecoloopsapp.data.model.RegisterRequest
import com.ecoloops.ecoloopsapp.data.remote.response.LoginResponse
import com.ecoloops.ecoloopsapp.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @POST("auth/register")
    fun register(
        @Body body: RegisterRequest
    ): Call<RegisterResponse>

    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @POST("auth/login")
    fun login(
        @Body body: LoginRequest
    ): Call<LoginResponse>
}