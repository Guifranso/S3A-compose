package br.com.arcom.s3a.data.repository

import android.content.Context
import br.com.arcom.s3a.database.dao.CronogramaDao
import br.com.arcom.s3a.database.db.DatabaseTransactionRunner
import br.com.arcom.s3a.database.entity.asExternalModel
import br.com.arcom.s3a.network.model.NetworkChecagem
import br.com.arcom.s3a.network.model.asEntity
import br.com.arcom.s3a.util.formatData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CronogramaRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
   private val cronogramaDataSource: CronogramaDataSource,
   private val cronogramaDao: CronogramaDao,
   private val locationRepository: LocationRepository,
) {

    suspend fun sendChecagem(km: Long, fotoBase64: String) {
        val network = NetworkChecagem(
            dataHora = LocalDateTime.now().formatData("dd/MM/yyyy HH:mm:ss"),
            km = km,
            fotoBase64 = fotoBase64
        )
        cronogramaDataSource.sendChecagem(network)
        locationRepository.sendLocation()
        cronogramaDao.insert(network.asEntity())
    }

    suspend fun salvarCronogramas() {
        val data = cronogramaDataSource.buscar()
        val cronogramas = data?.cronogramas?.map { it.asEntity() }

        databaseTransactionRunner {
            cronogramaDao.deleteAll()
            if (cronogramas != null) {
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