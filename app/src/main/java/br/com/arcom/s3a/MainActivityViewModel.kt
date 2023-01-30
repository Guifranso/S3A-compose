package br.com.arcom.s3a

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import br.com.arcom.s3a.auth.AuthManager
import br.com.arcom.s3a.auth.AuthState
import br.com.arcom.s3a.auth.AuthStateEnum
import br.com.arcom.s3a.data.domain.ObserveAuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    observeTraktAuthState: ObserveAuthState,
    private val authManager: AuthManager,
) : ViewModel() {

    val authState = MutableStateFlow(authManager.authState.value)
    val codSetor = authState.value.idSetor

    fun registrarAcesso(
        nomeAcessor: String?,
        idSetor: Long?
    ) {
        authManager.onNewAuthState(AuthState(nomeAcessor, idSetor))
    }

    val state: StateFlow<ViewState> = observeTraktAuthState.flow.map {
        ViewState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ViewState.Empty,
    )

    init {
        observeTraktAuthState(Unit)
    }

}

data class ViewState(
    val authState: AuthStateEnum? = null
) {
    companion object {
        val Empty = ViewState()
    }
}