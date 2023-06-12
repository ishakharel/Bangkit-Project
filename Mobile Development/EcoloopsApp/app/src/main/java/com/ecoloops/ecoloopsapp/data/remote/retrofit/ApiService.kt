package com.ecoloops.ecoloopsapp.data.remote.retrofit

import com.ecoloops.ecoloopsapp.data.model.EditPassRequest
import com.ecoloops.ecoloopsapp.data.model.EditProfileRequest
import com.ecoloops.ecoloopsapp.data.model.ForgotPassRequest
import com.ecoloops.ecoloopsapp.data.model.LoginRequest
import com.ecoloops.ecoloopsapp.data.model.RegisterRequest
import com.ecoloops.ecoloopsapp.data.model.ResetPassRequest
import com.ecoloops.ecoloopsapp.data.remote.response.DashboardResponse
import com.ecoloops.ecoloopsapp.data.remote.response.DetailCategoryResponse
import com.ecoloops.ecoloopsapp.data.remote.response.ForgotPassResponse
import com.ecoloops.ecoloopsapp.data.remote.response.ListCategoryResponse
import com.ecoloops.ecoloopsapp.data.remote.response.ListHistoryResponse
import com.ecoloops.ecoloopsapp.data.remote.response.ListMerchResponse
import com.ecoloops.ecoloopsapp.data.remote.response.LoginResponse
import com.ecoloops.ecoloopsapp.data.remote.response.LogoutResponse
import com.ecoloops.ecoloopsapp.data.remote.response.RegisterResponse
import com.ecoloops.ecoloopsapp.data.remote.response.ResetPassResponse
import com.ecoloops.ecoloopsapp.data.remote.response.UploadPhotoResponse
import com.ecoloops.ecoloopsapp.data.remote.response.UploadWasteResponse
import com.ecoloops.ecoloopsapp.data.remote.response.UserDetailResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

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
    @POST("auth/logout")
    fun logout(
        @Header("authorization") token: String
    ): Call<LogoutResponse>

    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @GET("user")
    fun getUserDetail(
        @Header("authorization") token: String
    ): Call<UserDetailResponse>

    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @PUT("user")
    fun editProfile(
        @Header("authorization") token: String,
        @Body body: EditProfileRequest
    ): Call<ResetPassResponse>

    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @PUT("auth/change-pass")
    fun editPassword(
        @Header("authorization") token: String,
        @Body body: EditPassRequest
    ): Call<ResetPassResponse>

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

    @Multipart
    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @PUT("user/profile")
    fun uploadPhoto(
        @Header("authorization") token: String,
        @Part file: MultipartBody.Part,
    ): Call<UploadPhotoResponse>

    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @GET("waste/histories")
    fun getHistories(
        @Header("authorization") token: String
    ): Call<ListHistoryResponse>

    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @GET("user/dashboard")
    fun getDashboard(
        @Header("authorization") token: String
    ): Call<DashboardResponse>

    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @GET("waste/histories/{id}")
    fun getHistoriesDetail(
        @Header("authorization") token: String,
        @Path("id") id: String
    ): Call<UploadWasteResponse>

    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @GET("waste/categories")
    fun getCategories(): Call<ListCategoryResponse>

    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @GET("waste/categories/{id}")
    fun getCategoriesDetail(
        @Header("authorization") token: String,
        @Path("id") id: String
    ): Call<DetailCategoryResponse>

    @Headers("apikey: B1sM1Llaht0pi5C4p5t0N3")
    @GET("user/merch")
    fun getListMerch(
        @Header("authorization") token: String,
    ): Call<ListMerchResponse>
}