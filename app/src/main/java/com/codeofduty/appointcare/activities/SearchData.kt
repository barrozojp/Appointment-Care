package com.codeofduty.appointcare.activities

import android.widget.Button

data class SearchData(
    val title: String,
    val logo: Int,
    val desc: String,
    var isExpandable: Boolean = false
)
