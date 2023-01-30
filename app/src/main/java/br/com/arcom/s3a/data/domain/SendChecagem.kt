package br.com.arcom.s3a.data.domain

import br.com.arcom.s3a.data.repository.CronogramaRepository
import br.com.arcom.s3a.di.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendChecagem @Inject constructor(
    private val cronogramaRepository: CronogramaRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<SendChecagem.Params>() {

    data class Params(
        val km: Long,
        val fotoBase64: String
    )

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            cronogramaRepository.sendChecagem(params.km,params.fotoBase64)
        }
    }
}