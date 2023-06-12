package com.ecoloops.ecoloopsapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListHistoryResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<ListHistoryItem?>? = null
) : Parcelable

@Parcelize
data class ListHistoryItem(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("points")
    val points: Int? = null,

    @field:SerializedName("image")
    val image: String? = null,
) : Parcelable
