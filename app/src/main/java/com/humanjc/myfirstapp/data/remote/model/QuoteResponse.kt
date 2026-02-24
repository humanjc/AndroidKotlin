package com.humanjc.myfirstapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class QuoteResponse(
    @SerializedName("slip") val slip: Slip
)

data class Slip(
    @SerializedName("id") val id: Int,
    @SerializedName("advice") val advice: String
)
