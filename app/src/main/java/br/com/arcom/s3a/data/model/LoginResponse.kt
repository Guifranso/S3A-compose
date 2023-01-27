package br.com.arcom.s3a.data.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String
)
