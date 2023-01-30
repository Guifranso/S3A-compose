package br.com.arcom.s3a.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arcom.repplus.ui.commons.UiMessageManager
import br.com.arcom.s3a.data.domain.BuscarCronogramas
import br.com.arcom.s3a.data.domain.SalvarCronogramas
import br.com.arcom.s3a.data.model.Cronograma
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
    private val buscarCronogramas: BuscarCronogramas,
    private val logger: Logger
) : ViewModel() {

    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    fun salvarCronogramas() {
        viewModelScope.launch {
            salvarCronogramas(SalvarCronogramas.Params())
                .collectStatus(loadingState, logger, uiMessageManager)
        }
    }

    val cronogramaUiState: StateFlow<CronogramaUiState> =
        buscarCronogramas.flow.asResult().map {
            when(it){
                is Result.Success -> CronogramaUiState.Success(it.data)
                is Result.Loading -> CronogramaUiState.Loading
                is Result.Error -> CronogramaUiState.Error
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CronogramaUiState.Loading
        )

    init {
        salvarCronogramas()
        buscarCronogramas(BuscarCronogramas.Params())
    }
}

sealed interface CronogramaUiState {
    data class Success(val cronogramas: List<Cronograma>) : CronogramaUiState
    object Error : CronogramaUiState
    object Loading : CronogramaUiState
}
