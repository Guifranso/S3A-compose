package br.com.arcom.s3a.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkLogin (
    val idSetor : Long,
    val idDeviceToken: String
)