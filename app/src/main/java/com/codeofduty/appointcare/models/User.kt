package com.codeofduty.appointcare.model

data class User(
    val _id: String,
    val role: String,
    val Fname: String,
    val Lname: String,
    val number: String,
    val gender: String,
    val age: Int,
    val email: String,
    val password: String,
    val __v: Int
)
