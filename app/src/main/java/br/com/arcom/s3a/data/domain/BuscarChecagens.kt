package br.com.arcom.s3a.data.domain

import br.com.arcom.s3a.data.model.ChecagensRealizadas
import br.com.arcom.s3a.data.repository.CronogramaRepository
import br.com.arcom.s3a.di.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BuscarChecagens @Inject constructor(
    private val cronogramaRepository: CronogramaRepository,
    private val dispatchers: AppCoroutineDispatchers
) : SubjectInteractor<BuscarChecagens.Params, ChecagensRealizadas>() {

    class Params()
    override suspend fun createObservable(params: Params): Flow<ChecagensRealizadas> {
        return withContext(dispatchers.io) {
            cronogramaRepository.findCronogramas().map {
                ChecagensRealizadas(
                    checagemInicial = it.firstOrNull(),
                    checagemFinal = if (it.size >= 2) it[1] else null
                )
            }
        }
    }
}