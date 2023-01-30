package br.com.arcom.s3a.ui.commons

import android.util.Log
import br.com.arcom.repplus.ui.commons.UiMessage
import br.com.arcom.repplus.ui.commons.UiMessageManager
import br.com.arcom.s3a.data.domain.InvokeError
import br.com.arcom.s3a.data.domain.InvokeStarted
import br.com.arcom.s3a.data.domain.InvokeStatus
import br.com.arcom.s3a.data.domain.InvokeSuccess
import br.com.arcom.s3a.util.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger

class ObservableLoadingCounter {
    private val count = AtomicInteger()
    private val loadingState = MutableStateFlow(count.get())

    val observable: Flow<Boolean>
        get() = loadingState.map { it > 0 }.distinctUntilChanged()

    fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }
}

suspend fun Flow<InvokeStatus>.collectStatus(
    counter: ObservableLoadingCounter,
    logger: Logger? = null,
    uiMessageManager: UiMessageManager? = null
) = collect { status ->
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> counter.removeLoader()
        is InvokeError -> {
            logger?.i(status.throwable)
            Log.d("exception", status.throwable.message?: "")
            uiMessageManager?.emitMessage(UiMessage(status.throwable))
            counter.removeLoader()
        }
        else -> { counter.removeLoader() }
    }
}

suspend fun Flow<InvokeStatus>.collectStatus(
    counter: ObservableLoadingCounter,
    logger: Logger? = null,
    uiMessageManager: UiMessageManager? = null,
    callback: () -> Unit,
    callbackError: (() -> Unit)? = null
) = collect { status ->
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> {
            counter.removeLoader()
            callback()
        }
        is InvokeError -> {
            logger?.i(status.throwable)
            uiMessageManager?.emitMessage(UiMessage(status.throwable))
            counter.removeLoader()
            if (callbackError != null) {
                callbackError()
            }
        }
        else -> { counter.removeLoader() }
    }
}