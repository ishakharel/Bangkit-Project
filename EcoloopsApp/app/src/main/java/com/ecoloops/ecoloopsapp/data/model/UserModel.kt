package com.ecoloops.ecoloopsapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel (
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var image: String? = null,
    var points: Int? = 0,
    var token: String? = null
    ) : Parcelable