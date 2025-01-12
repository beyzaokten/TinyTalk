package com.example.tinytalk.model

data class Story(
    val id: Int,
    val story: String,
    val missingWords: List<String>,
    val image: String
)
