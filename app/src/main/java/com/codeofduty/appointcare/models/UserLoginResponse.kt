package com.codeofduty.appointcare.models

data class UserLoginResponse(
    val token: String,
    val user: UserX
)