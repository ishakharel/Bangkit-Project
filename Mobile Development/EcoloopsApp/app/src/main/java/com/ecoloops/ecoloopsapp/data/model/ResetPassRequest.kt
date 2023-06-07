package com.ecoloops.ecoloopsapp.data.model

data class ResetPassRequest (
    var email : String,
    var newPassword : String,
    var otp : String,
)