package br.com.arcom.s3a.data.repository

import br.com.arcom.s3a.network.bodyOrThrow
import br.com.arcom.s3a.network.model.NetworkLocation
import br.com.arcom.s3a.network.services.CronogramaService
import br.com.arcom.s3a.network.services.LocationService
import br.com.arcom.s3a.network.withRetry
import javax.inject.Inject

class CronogramaDataSource @Inject constructor(
    private val cronogramaService: CronogramaService
) {

    suspend fun buscar() = withRetry {
        cronogramaService.buscar().bodyOrThrow()
    }

}