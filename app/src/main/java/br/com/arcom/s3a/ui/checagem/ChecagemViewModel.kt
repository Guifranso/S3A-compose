package br.com.arcom.s3a.ui.checagem

import android.graphics.Bitmap
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arcom.repplus.ui.commons.UiMessage
import br.com.arcom.repplus.ui.commons.UiMessageManager
import br.com.arcom.s3a.R
import br.com.arcom.s3a.data.domain.SendChecagem
import br.com.arcom.s3a.data.model.TipoChecagem
import br.com.arcom.s3a.ui.checagem.navigation.ChecagemDestination
import br.com.arcom.s3a.ui.commons.ObservableLoadingCounter
import br.com.arcom.s3a.ui.commons.collectStatus
import br.com.arcom.s3a.util.Logger
import br.com.arcom.s3a.util.getNumbers
import br.com.arcom.s3a.util.toBase64
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChecagemViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    private val sendChecagem: SendChecagem,
    private val logger: Logger
) : ViewModel() {

    private val idTipoChecagem: String = checkNotNull(
        savedStateHandle[ChecagemDestination.tipoChecagem]
    )
    private val tipoChecagem = TipoChecagem.getById(idTipoChecagem.toLong())

    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val listaEtapas = listOf<EtapasState>(
        EtapasState(
            index = 0,
            title = R.string.quilometragem,
            showPrevious = false,
            showDone = true,
        ),
        EtapasState(
            index = 1,
            title = R.string.finalizar,
            showPrevious = true,
            showDone = true,
        )
    )
    private val _fields = MutableStateFlow<ChecagemFields>(ChecagemFields(listaEtapas))
    val fieldsState = _fields.asStateFlow()

    fun validarEtapas(fields: ChecagemFields): Boolean {
        return when (fields.currentIndex) {
            0 -> { fields.km != null }
            1 -> { fields.validado }
            else -> false
        }
    }

    fun recognizeText(btp: Bitmap) {
        val fields = _fields.value
        FirebaseVision.getInstance().onDeviceTextRecognizer.processImage(
            FirebaseVisionImage.fromBitmap(
                btp
            )
        ).addOnSuccessListener { firebaseVisionText ->
            viewModelScope.launch {
                fields.validado = firebaseVisionText.textBlocks.flatMap { it.lines }
                    .map { it.text.getNumbers() }
                    .any { it == fields.km }
                if (fields.validado) {
                    fields.bitmap = btp
                }else{
                    emitMessage("Não foi possível analisar a foto!")
                }
            }
        }.addOnFailureListener { e ->
            e.printStackTrace()
            emitMessage("Erro ao verificar imagem!")
        }
    }

    fun sendChecagemFoto(callback: () -> Unit) {
        val fields = _fields.value
        viewModelScope.launch {
            if (fields.validado && fields.km != null && fields.bitmap != null) {
                sendChecagem(
                    SendChecagem.Params(
                        fields.km!!,
                        fields.bitmap!!.toBase64()
                    )
                ).collectStatus(
                    loadingState,
                    logger,
                    uiMessageManager,
                    callback = {
                        callback()
                    }
                )
            }
        }
    }

    fun emitMessage(message: String) {
        viewModelScope.launch {
            uiMessageManager.emitMessage(UiMessage(message))
        }
    }

    fun clearMessages(){
        viewModelScope.launch {
            uiMessageManager.clearAll()
        }
    }

    val checagemUiState: StateFlow<ChecagemUiState> =
        combine(
            loadingState.observable,
            uiMessageManager.message
        ) { loading, uiMessage ->
            ChecagemUiState(
                loading, uiMessage,tipoChecagem
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChecagemUiState.Empty
        )
}

data class ChecagemFields(
    val etapas: List<EtapasState>
) {
    var currentIndex by mutableStateOf(0)
    var validado by mutableStateOf(false)
    var km by mutableStateOf<Long?>(null)
    var bitmap by mutableStateOf<Bitmap?>(null)
}

@Stable
class EtapasState(
    val index: Int,
    val title: Int,
    val showPrevious: Boolean,
    val showDone: Boolean
)

data class ChecagemUiState(
    val loading: Boolean = false,
    val uiMessage: UiMessage? = null,
    val tipoChecagem: TipoChecagem? = TipoChecagem.INICIO
) {
    companion object {
        val Empty = ChecagemUiState()
    }
}