package com.ecoloops.ecoloopsapp.data.model

data class EditPassRequest(
    val oldPassword: String,
    val newPassword: String,
)