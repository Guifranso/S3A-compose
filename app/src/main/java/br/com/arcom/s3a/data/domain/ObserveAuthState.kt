package br.com.arcom.s3a.data.domain

import br.com.arcom.s3a.auth.AuthManager
import br.com.arcom.s3a.auth.AuthStateEnum
import br.com.arcom.s3a.data.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ObserveAuthState @Inject constructor(
    private val traktManager: AuthManager
) : SubjectInteractor<Unit, AuthStateEnum>() {
    override suspend fun createObservable(params: Unit): Flow<AuthStateEnum> {
        return traktManager.state
    }
}