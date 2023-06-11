package com.ecoloops.ecoloopsapp.data.model

data class EditProfileRequest(
    val email: String,
    val newUsername: String,
    val address: String,
    val gender: String,
    val age: Int,
    val job: String,
    val dob: String,
    )