package com.codeofduty.appointcare.activities

class HistoryDoctorData (

    val status: String? = null,
    val fullName: String? = null,
    val email: String? = null,
    val number: String? = null,
    val doctorId: String? = null,
    val localDate: Any? = null,
    val localTime: Any? = null,
    val imageData: String?,
    val __v: String? = null,
    val _id: String? = null, //THIS IS THE BOOKING ID

    //CONSULTATION DETAILS
    val symptoms: List<String>? = null,
    val observation: String? = null,
    val prescription: String? = null

)
