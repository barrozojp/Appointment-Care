package com.codeofduty.appointcare.models

import com.google.gson.annotations.SerializedName

data class ImageData(
    @SerializedName("type")
    val type: String? = null,

    @SerializedName("data")
    val data: List<Int>? = null
)