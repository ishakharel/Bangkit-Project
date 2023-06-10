package com.ecoloops.ecoloopsapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadWasteResponse(

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: DataUploadWaste,
) : Parcelable

@Parcelize
data class DataUploadWaste(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("description_recycle")
    val description_recycle: String,

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("points")
    val points: Int,

    @field:SerializedName("image")
    val image: String
): Parcelable