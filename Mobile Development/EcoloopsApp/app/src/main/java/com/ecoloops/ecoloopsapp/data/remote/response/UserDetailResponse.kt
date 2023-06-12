package com.ecoloops.ecoloopsapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetailResponse(

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: DataUser,
) : Parcelable

@Parcelize
data class DataUser(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("address")
    val address: String,

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("age")
    val age: String,

    @field:SerializedName("job")
    val job: String,

    @field:SerializedName("dob")
    val dob: String,

    @field:SerializedName("image")
    val image: String,
) : Parcelable