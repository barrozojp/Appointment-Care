package com.codeofduty.appointcare.models

data class Schedule(
    val __v: Int? = null,
    val _id: String? = null,
    val date: String? = null,
    val doctorId: String? = null,
    val email: String? = null,
    val fullName: String? = null,
    val localDate: Any? = null,
    val localTime: Any? = null,
    val number: String? = null,
    val online: Boolean? = null,
    val f2f: Boolean? = null,
    val patientId: String? = null,
    val status: String? = null,
    val time: String? = null,
    val imageData: ImageData?,


    //CONSULTATION DETAILS
    val symptoms: List<String>? = null,
    val observation: String? = null,
    val prescription: String? = null
)