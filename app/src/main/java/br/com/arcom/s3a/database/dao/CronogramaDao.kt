package br.com.arcom.s3a.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.arcom.s3a.database.entity.CronogramaEntity

@Dao
abstract class CronogramaDao : EntityDao<CronogramaEntity>() {

    @Query("SELECT * from cronogramas")
    abstract fun buscarCronogramas(): PagingSource<Int, CronogramaEntity>

    @Query("SELECT * from cronogramas WHERE id = :id")
    abstract fun buscarCronogramasPorId(id: Long): PagingSource<Int, CronogramaEntity>

    @Insert
    abstract suspend fun criarCronograma(cronograma: CronogramaEntity)

    @Delete
    abstract suspend fun deleteCronograma(cronograma: CronogramaEntity)
}