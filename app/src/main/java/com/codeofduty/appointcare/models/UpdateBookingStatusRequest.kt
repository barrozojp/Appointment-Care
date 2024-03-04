package com.codeofduty.appointcare.models

data class UpdateBookingStatusRequest(
    val patientId: String,
    val status: String
)
