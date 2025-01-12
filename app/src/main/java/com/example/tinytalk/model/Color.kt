package com.example.tinytalk.model

import com.google.gson.annotations.SerializedName

data class Color(
    @SerializedName("turkish") val turkishName: String,
    @SerializedName("english") val englishName: String,
    @SerializedName("image") val imageName: String
)
