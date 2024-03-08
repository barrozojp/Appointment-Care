package com.codeofduty.appointcare.models

data class UpdateSympConsPres(
    val observation: String,
    val prescription: String,
    val symptoms: List<String>
)