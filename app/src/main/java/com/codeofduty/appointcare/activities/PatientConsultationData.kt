package com.codeofduty.appointcare.activities

data class PatientConsultationData(

    val status: String? = null,
    val fullName: String? = null,
    val specialty: String? = null,
    val mdYear: String? = null,
    val email: String? = null,
    val number: String? = null,
    val consultPrice: String? = null,
    val f2f: String? = null,
    val date: String? = null,
    val time: String? = null,
    val address: String? = null,
    val doctorId: String? = null,
    val localDate: Any? = null,
    val localTime: Any? = null,
    val imageData: String?,
    val __v: String? = null,
    val _id: String? = null,
    val bookingID: String? = null,

    //CONSULTATION DETAILS
    val symptoms: List<String>? = null,
    val observation: String? = null,
    val prescription: String? = null
)
