package br.com.arcom.s3a.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arcom.repplus.ui.commons.UiMessage
import br.com.arcom.repplus.ui.commons.UiMessageManager
import br.com.arcom.s3a.auth.AuthManager
import br.com.arcom.s3a.auth.AuthState
import br.com.arcom.s3a.data.domain.BuscarChecagens
import br.com.arcom.s3a.data.domain.SalvarCronogramas
import br.com.arcom.s3a.data.model.ChecagensRealizadas
import br.com.arcom.s3a.data.result.Result
import br.com.arcom.s3a.data.result.asResult
import br.com.arcom.s3a.ui.commons.ObservableLoadingCounter
import br.com.arcom.s3a.ui.commons.collectStatus
import br.com.arcom.s3a.util.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject internal constructor(
    private val salvarCronogramas: SalvarCronogramas,
    private val buscarChecagens: BuscarChecagens,
    private val logger: Logger,
    private val authManager: AuthManager
) : ViewModel() {

    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    fun updateChecagens() {
        viewModelScope.launch {
            salvarCronogramas(SalvarCronogramas.Params())
                .collectStatus(loadingState, logger, uiMessageManager)
        }
    }

    fun clearMessages() {
        viewModelScope.launch {
            uiMessageManager.clearAll()
        }
    }

    val menuUiState: StateFlow<MenuUiState> =
        combine(
            buscarChecagens.flow.asResult(),
            authManager.authState,
            loadingState.observable,
            uiMessageManager.message
        ) { checagens, authManager, loading, message ->
            val checagem = when (checagens) {
                is Result.Success -> ChecagensUiState.Success(checagens.data)
                is Result.Loading -> ChecagensUiState.Loading
                is Result.Error -> ChecagensUiState.Error
            }
            MenuUiState(
                checagem,
                authManager,
                loading,
                message
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MenuUiState.Empty
        )

    init {
        updateChecagens()
        buscarChecagens(BuscarChecagens.Params())
    }
}

data class MenuUiState(
    val checagensUiState: ChecagensUiState = ChecagensUiState.Loading,
    val authState: AuthState? = null,
    val loading: Boolean = false,
    val uiMessage: UiMessage? = null
) {
    companion object {
        val Empty = MenuUiState()
    }
}

sealed interface ChecagensUiState {
    data class Success(val checagens: ChecagensRealizadas) : ChecagensUiState
    object Error : ChecagensUiState
    object Loading : ChecagensUiState
}
