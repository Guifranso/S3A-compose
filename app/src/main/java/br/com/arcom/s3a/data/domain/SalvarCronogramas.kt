package br.com.arcom.s3a.data.domain

import br.com.arcom.s3a.data.repository.CronogramaRepository
import br.com.arcom.s3a.di.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SalvarCronogramas @Inject constructor(
    private val cronogramaRepository: CronogramaRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<SalvarCronogramas.Params>() {

    class Params

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            cronogramaRepository.salvarCronogramas()
        }
    }
}