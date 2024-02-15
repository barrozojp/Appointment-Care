package com.codeofduty.appointcare

data class SearchData(
    val title: String,
    val logo: Int,
    val desc: String,
    var isExpandable: Boolean = false
)