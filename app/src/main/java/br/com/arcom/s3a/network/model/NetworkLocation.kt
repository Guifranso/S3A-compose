package br.com.arcom.s3a.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkLocation (
    val longitude : Double,
    val latitude: Double
)