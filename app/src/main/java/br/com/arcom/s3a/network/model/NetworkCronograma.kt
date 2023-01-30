package br.com.arcom.s3a.network.model

import br.com.arcom.s3a.database.entity.CronogramaEntity
import br.com.arcom.s3a.util.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class NetworkCronograma(
    val idSetor: Long,
    @Serializable(LocalDateTimeSerializer::class)
    val dataHora: LocalDateTime,
    val foto: String,
    val km: Long,
)

fun NetworkCronograma.asEntity() = CronogramaEntity(
    0,
    dataHora,
    foto,
    km,
)