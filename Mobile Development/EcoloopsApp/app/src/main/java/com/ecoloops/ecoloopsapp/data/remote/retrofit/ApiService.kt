package com.ecoloops.ecoloopsapp.data.remote.retrofit

import com.ecoloops.ecoloopsapp.data.model.ForgotPassRequest
import com.ecoloops.ecoloopsapp.data.model.LoginRequest
import com.ecoloops.ecoloopsapp.data.model.RegisterRequest
import com.ecoloops.ecoloopsapp.data.model.ResetPassRequest
import com.ecoloops.ecoloopsapp.data.remote.response.ForgotPassResponse
import com.ecoloops.ecoloopsapp.data.remote.response.LoginResponse
import com.ecoloops.ecoloopsapp.data.remote.response.RegisterResponse
import com.ecoloops.ecoloopsapp.data.remote.response.ResetPassResponse
import com.ecoloops.ecoloopsapp.data.remote.response.UploadWasteResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

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

    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @POST("auth/forgot-pass")
    fun forgotPass(
        @Body body: ForgotPassRequest
    ): Call<ForgotPassResponse>

    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @PUT("auth/reset-pass")
    fun resetPass(
        @Body body: ResetPassRequest
    ): Call<ResetPassResponse>

    @Multipart
    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @POST("waste/upload")
    fun uploadWaste(
        @Header("authorization") token: String,
        @Part file: MultipartBody.Part,
    ): Call<UploadWasteResponse>
}