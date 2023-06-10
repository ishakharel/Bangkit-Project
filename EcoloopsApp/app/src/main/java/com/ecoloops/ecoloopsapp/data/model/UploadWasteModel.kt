package com.ecoloops.ecoloopsapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UploadWasteModel (
    var id: String? = null,
    var name: String? = null,
    var category: String? = null,
    var description_recycle: String? = null,
    var date: String? = null,
    var points: Int? = 0,
    var image: String? = null,
) : Parcelable