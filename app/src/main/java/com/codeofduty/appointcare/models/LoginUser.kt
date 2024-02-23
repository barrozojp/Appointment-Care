package com.codeofduty.appointcare.models

data class LoginUser(
    val role: String? = null,
    val email: String,
    val password: String,
)
