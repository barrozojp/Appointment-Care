package com.codeofduty.appointcare.models

data class UpdateSympConsPres(
    val observation: String,
    val patientId: String,
    val prescription: String,
    val symptoms: List<String>
)