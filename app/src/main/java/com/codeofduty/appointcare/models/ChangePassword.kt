package com.codeofduty.appointcare.models

data class ChangePassword(
    val currentPassword: String,
    val newPassword: String
)