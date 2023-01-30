package br.com.arcom.s3a.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.arcom.s3a.data.model.Cronograma
import java.time.LocalDateTime

@Entity(tableName = "cronogramas")
data class CronogramaEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override var id: Long,
    @ColumnInfo(name = "dataHora") val dataHora: LocalDateTime,
    @ColumnInfo(name = "foto") val foto: String,
    @ColumnInfo(name = "km") val km: Long
) : AppEntity

fun CronogramaEntity.asExternalModel() = Cronograma(
    id,
    dataHora,
    foto,
    km,
)