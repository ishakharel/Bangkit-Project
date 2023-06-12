package com.ecoloops.ecoloopsapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListMerchResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<ListMerchItem?>? = null
) : Parcelable

@Parcelize
data class ListMerchItem(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("points")
    val points: Int? = null,

    @field:SerializedName("stok")
    val stok: Int? = null,
) : Parcelable