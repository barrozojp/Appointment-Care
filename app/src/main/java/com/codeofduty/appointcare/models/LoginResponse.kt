package com.codeofduty.appointcare.models

import User
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("user")
    val user: User,

    @SerializedName("token")
    val token: String
)