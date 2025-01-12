package com.example.tinytalk.model

data class Message(
    val text: String,
    val isUserMessage: Boolean // true: kullanıcı mesajı, false: bot mesajı
)
