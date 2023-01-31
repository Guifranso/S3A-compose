package br.com.arcom.s3a.ui.checagem

import android.graphics.Bitmap
import android.location.Location
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arcom.repplus.ui.commons.UiMessage
import br.com.arcom.repplus.ui.commons.UiMessageManager
import br.com.arcom.s3a.data.domain.SendChecagem
import br.com.arcom.s3a.data.result.Result
import br.com.arcom.s3a.data.result.asResult
import br.com.arcom.s3a.ui.commons.ObservableLoadingCounter
import br.com.arcom.s3a.ui.commons.collectStatus
import br.com.arcom.s3a.ui.menu.ChecagensUiState
import br.com.arcom.s3a.util.Logger
import br.com.arcom.s3a.util.toBase64
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ChecagemViewModel @Inject internal constructor(
    private val sendChecagem: SendChecagem,
    private val logger: Logger
) : ViewModel() {

    val validado = MutableStateFlow(false)
    val km = MutableStateFlow<Long?>(null)
    val bitmap = MutableStateFlow<Bitmap?>(null)
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    fun recognizeText(btp: Bitmap, inputKm: String) {
        FirebaseVision.getInstance().onDeviceTextRecognizer.processImage(
            FirebaseVisionImage.fromBitmap(
                btp
            )
        ).addOnSuccessListener { firebaseVisionText ->
            viewModelScope.launch {
                validado.emit(
                    firebaseVisionText.textBlocks.flatMap { it.lines }
                        .map { it.text.toIntOrNull() }.any { it == inputKm!!.toIntOrNull() }
                )
                firebaseVisionText.textBlocks.flatMap { it.lines }
                    .map { Log.d("KILO",it.text)}
                if (validado.value) {
                    km.emit(inputKm.toLong())
                    bitmap.emit(btp)
                }
            }
        }.addOnFailureListener { e ->
            Log.e("MainActivity", "Error: $e")
        }
    }

    fun sendChecagem() {
        viewModelScope.launch {
            if (validado.value) {
                sendChecagem(
                    SendChecagem.Params(
                        km.value!!,
                        bitmap.value!!.toBase64()
                    )
                ).collectStatus(
                    loadingState, logger, uiMessageManager
                )
            }
        }
    }

    val checagemUiState: StateFlow<ChecagemUiState> =
        combine(loadingState.observable, uiMessageManager.message, validado) { loading, uiMessage, validado ->
            ChecagemUiState(
                loading, uiMessage, validado
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChecagemUiState.Empty
        )
}

data class ChecagemUiState(
    val loading: Boolean = false,
    val uiMessage: UiMessage? = null,
    val validado: Boolean = false,
) {
    companion object {
        val Empty = ChecagemUiState()
    }
}