package br.com.arcom.s3a.data.domain

import br.com.arcom.s3a.data.model.Cronograma
import br.com.arcom.s3a.data.repository.CronogramaRepository
import br.com.arcom.s3a.di.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BuscarCronogramas @Inject constructor(
    private val cronogramaRepository: CronogramaRepository,
    private val dispatchers: AppCoroutineDispatchers
) : SubjectInteractor<BuscarCronogramas.Params, List<Cronograma>>() {

    class Params()

    override suspend fun createObservable(params: Params): Flow<List<Cronograma>> {
        return withContext(dispatchers.io) {
            cronogramaRepository.findCronogramas()
        }
    }
}