package com.codeofduty.appointcare.models

data class EditSchedule(
    val date: String,
    val online: Boolean,
    val status: String,
    val time: String
)