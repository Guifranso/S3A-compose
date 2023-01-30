package br.com.arcom.s3a.data.model

import android.content.Context
import com.google.gson.Gson
import java.time.LocalDateTime

data class Cronograma(
    val id: Long,
    val dataHora: LocalDateTime,
    val foto: String,
    val km: Long,
)