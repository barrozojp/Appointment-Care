package com.codeofduty.appointcare.activities

data class RejectedBookingsData(
    val status: String? = null,
    val imageData: String?,
    val fullName: String? = null,
    val number: String? = null,
    val email: String? = null,
    val online: String? = null,
    val date: String? = null,
    val time: String? = null,
    val bookingsID: String? = null,
    val patientId: String? = null,
    val doctorId: String? = null,
    val symptoms: String? = null,
    val observation: String? = null,
    val prescription: String? = null,
    val __v: Int? = null
)
