package com.example.tinytalk.model

data class MatchPair(
    var id: Int = 0,
    var type: String = "",      // "image" veya "text"
    val name: String = "",
    val image: String = ""
)
