package com.codeofduty.appointcare.activities

import com.codeofduty.appointcare.models.ImageData

data class SearchData(
    val title: String,
    val _id: String,
    val specialty: String,
    val imageData: String?,
    val num: String,
    val email: String,
    val location: String,
    val cnsltPrice: String,
    var isExpandable: Boolean = false
)
