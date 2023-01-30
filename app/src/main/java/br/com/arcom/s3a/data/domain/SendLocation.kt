package br.com.arcom.s3a.data.domain

import br.com.arcom.s3a.auth.AuthManager
import br.com.arcom.s3a.auth.AuthStateEnum
import br.com.arcom.s3a.data.domain.SubjectInteractor
import br.com.arcom.s3a.data.repository.LocationRepository
import br.com.arcom.s3a.di.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SendLocation @Inject constructor(
    private val locationRepository: LocationRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<SendLocation.Params>() {

    class Params()

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io){
            locationRepository.sendLocation()
        }
    }
}