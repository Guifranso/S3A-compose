package br.com.arcom.s3a.data.repository

import android.content.Context
import br.com.arcom.s3a.database.dao.CronogramaDao
import br.com.arcom.s3a.database.db.DatabaseTransactionRunner
import br.com.arcom.s3a.database.entity.asExternalModel
import br.com.arcom.s3a.network.model.asEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CronogramaRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    val cronogramaDataSource: CronogramaDataSource,
    val cronogramaDao: CronogramaDao
) {

    suspend fun salvarCronogramas() {
        val data = cronogramaDataSource.buscar()
        val cronogramas = data?.cronogramas?.map { it.asEntity() }

        if (cronogramas?.isNotEmpty() == true) {
            databaseTransactionRunner {
                cronogramaDao.deleteAll()
                cronogramaDao.insertAll(cronogramas)
            }
        }
    }

    fun findCronogramas() = cronogramaDao.buscarCronogramasStream().map {
        it.map { cronograma ->
            cronograma.asExternalModel()
        }
    }

}