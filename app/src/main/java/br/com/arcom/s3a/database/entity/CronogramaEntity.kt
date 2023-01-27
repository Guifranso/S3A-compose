package br.com.arcom.s3a.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.arcom.s3a.data.model.Cronograma

@Entity(tableName = "cronogramas")
data class CronogramaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id"           ) override var id: Long,
    @ColumnInfo(name = "nome"         ) val nome: String,
    @ColumnInfo(name = "usuario"      ) val usuario: String,
    @ColumnInfo(name = "data_inicio"  ) val dataInicio: String,
    @ColumnInfo(name = "data_fim"     ) val dataFim: String,
    @ColumnInfo(name = "dia_atual"    ) val diaAtual: Int,
) : AppEntity

fun CronogramaEntity.asExternalModel() = Cronograma(
    id,
    usuario,
    dataInicio,
    dataFim,
    diaAtual,
)