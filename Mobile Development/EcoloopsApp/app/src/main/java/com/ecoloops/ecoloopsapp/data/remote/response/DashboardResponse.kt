package com.ecoloops.ecoloopsapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DashboardResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: DashboardData? = null
) : Parcelable

@Parcelize
data class DashboardData(
    @field:SerializedName("rewards")
    val rewards: Int? = null,

    @field:SerializedName("scan")
    val scan: Int? = null,

    @field:SerializedName("points")
    val points: Int? = null
) : Parcelable
