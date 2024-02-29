package com.codeofduty.appointcare.models

data class DoctorDetailsBookAppoint(
    val Fname: String,
    val Lname: String,
    val _id: String? = null,
    val specialty: String,
    val md: Int,
)
