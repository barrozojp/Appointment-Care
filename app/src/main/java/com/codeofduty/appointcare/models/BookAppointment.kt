package com.codeofduty.appointcare.models

data class BookAppointment(
    val date: String,
    val doctorId: String,
    val email: String,
    val fullName: String,
    val number: String,
    val online: String,
    val time: String
)