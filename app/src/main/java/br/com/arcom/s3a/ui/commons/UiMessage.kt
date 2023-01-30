package br.com.arcom.repplus.ui.commons

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.net.UnknownHostException
import java.util.UUID

data class UiMessage(
    val message: String,
    val id: Long = UUID.randomUUID().mostSignificantBits,
)

fun UiMessage(
    t: Throwable,
    id: Long = UUID.randomUUID().mostSignificantBits,
): UiMessage = UiMessage(
    message = translateException(t),
    id = id,
)

private fun translateException(ex: Throwable): String {
    return when(ex) {
        is UnknownHostException -> "Possivelmente você está sem internet!"
        else -> ex.message ?: "Erro ocorrido: $ex"
    }
}

class UiMessageManager {
    private val mutex = Mutex()

    private val _messages = MutableStateFlow(emptyList<UiMessage>())

    /**
     * A flow emitting the current message to display.
     */
    val message: Flow<UiMessage?> = _messages.map { it.firstOrNull() }.distinctUntilChanged()

    suspend fun emitMessage(message: UiMessage) {
        mutex.withLock {
            _messages.value = _messages.value + message
        }
    }

    suspend fun clearMessage(id: Long) {
        mutex.withLock {
            _messages.value = _messages.value.filterNot { it.id == id }
        }
    }

    suspend fun clearAll() {
        mutex.withLock {
            _messages.value = emptyList()
        }
    }
}