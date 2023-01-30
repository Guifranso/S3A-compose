package br.com.arcom.s3a.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arcom.repplus.ui.commons.UiMessageManager
import br.com.arcom.s3a.data.domain.BuscarChecagens
import br.com.arcom.s3a.data.domain.SalvarCronogramas
import br.com.arcom.s3a.data.model.ChecagensRealizadas
import br.com.arcom.s3a.data.result.asResult
import br.com.arcom.s3a.ui.commons.ObservableLoadingCounter
import br.com.arcom.s3a.ui.commons.collectStatus
import br.com.arcom.s3a.util.Logger
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import br.com.arcom.s3a.data.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MenuViewModel @Inject internal constructor(
    private val salvarCronogramas: SalvarCronogramas,
    private val buscarChecagens: BuscarChecagens,
    private val logger: Logger
) : ViewModel() {

    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    fun updateChecagens() {
        viewModelScope.launch {
            salvarCronogramas(SalvarCronogramas.Params())
                .collectStatus(loadingState, logger, uiMessageManager)
        }
    }

    val checagensUiState: StateFlow<ChecagensUiState> =
        buscarChecagens.flow.asResult().map {
            when(it){
                is Result.Success -> ChecagensUiState.Success(it.data)
                is Result.Loading -> ChecagensUiState.Loading
                is Result.Error -> ChecagensUiState.Error
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChecagensUiState.Loading
        )

    init {
        updateChecagens()
        buscarChecagens(BuscarChecagens.Params())
    }
}

sealed interface ChecagensUiState {
    data class Success(val checagens: ChecagensRealizadas) : ChecagensUiState
    object Error : ChecagensUiState
    object Loading : ChecagensUiState
}
