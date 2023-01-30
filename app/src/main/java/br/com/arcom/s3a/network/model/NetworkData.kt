package br.com.arcom.s3a.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkData (
    val cronogramas: List<NetworkCronograma>
)