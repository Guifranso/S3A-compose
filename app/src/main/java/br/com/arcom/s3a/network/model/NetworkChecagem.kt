package br.com.arcom.s3a.network.model

import br.com.arcom.s3a.database.entity.CronogramaEntity
import br.com.arcom.s3a.util.formatDateTime
import kotlinx.serialization.Serializable

@Serializable
data class NetworkChecagem(
    val dataHora: String,
    val km: Long,
    val fotoBase64: String,
)

fun NetworkChecagem.asEntity() = CronogramaEntity(
    id = 0,
    foto = fotoBase64,
    km = km,
    dataHora = dataHora.formatDateTime("dd/MM/yyyy HH:mm:ss")
)