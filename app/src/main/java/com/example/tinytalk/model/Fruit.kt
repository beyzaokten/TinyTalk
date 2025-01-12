package com.example.tinytalk.model

import com.google.gson.annotations.SerializedName

data class Fruit(
    @SerializedName("turkish") val turkishName: String,
    @SerializedName("english") val englishName: String,
    @SerializedName("image") val imageName: String
)
