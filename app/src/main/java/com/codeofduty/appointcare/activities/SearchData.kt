package com.codeofduty.appointcare.activities

import android.widget.Button

data class SearchData(
    val title: String,
    val logo: Int,
    val num: String,
    val email: String,
    var isExpandable: Boolean = false
)
