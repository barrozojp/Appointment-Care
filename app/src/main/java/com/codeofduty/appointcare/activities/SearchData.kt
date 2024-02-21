package com.codeofduty.appointcare.activities

import android.widget.Button

data class SearchData(
    val title: String,
    val specialty: String,
    val logo: Int,
    val num: String,
    val email: String,
    val location: String,
    val cnsltPrice: String,
    var isExpandable: Boolean = false
)
