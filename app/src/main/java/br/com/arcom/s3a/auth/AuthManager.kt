package br.com.arcom.s3a.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.arcom.s3a.di.AppCoroutineDispatchers
import br.com.arcom.s3a.network.NetworkS3aApi
import kotlinx.coroutines.DelicateCoroutinesApi
import dagger.Lazy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@DelicateCoroutinesApi
@Singleton
class AuthManager @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val networkRepApi: Lazy<NetworkS3aApi>,
    @Named("auth") private val authPrefs: SharedPreferences,
    @Named("ultimaVersaoApp") private val ultVersaoPrefs: SharedPreferences,
    @Named("tokenPlay") private val tokenPlayPrefs: SharedPreferences
) {
    private val _authState = MutableStateFlow(EmptyAuthState)
    val authState: StateFlow<AuthState>
        get() = _authState.asStateFlow()
    private val _state = MutableStateFlow(AuthStateEnum.LOADING)
    val state: StateFlow<AuthStateEnum>
        get() = _state.asStateFlow()

    private val _ultimaVersao = MutableStateFlow("")
    val ultimaVersao: StateFlow<String>
        get() = _ultimaVersao.asStateFlow()

    init {

        GlobalScope.launch(dispatchers.io) {
            authState.collect { authState ->
                updateAuthState(authState)

                networkRepApi.get().apply {
                    authState.idSetor?.let { atualizarSetor(it) }
                    tokenPlayPrefs.getString("tokenPlay", null)?.let { atualizarTokenPlay(it) }
                }
            }
        }
        // Read the auth state from prefs
        GlobalScope.launch(dispatchers.main) {
            val state = withContext(dispatchers.io) { readAuthState() }
            _authState.value = state
            updateAuthState(state)

            val stateLast = withContext(dispatchers.io) { ultVersaoPrefs.getString("ultimaVersaoApp", "") }
            _ultimaVersao.value = stateLast!!
        }

    }

    private fun updateAuthState(authState: AuthState) {
        if(! authState.loading) {
            if (authState.isAuthorized()) {
                _state.value = AuthStateEnum.LOGGED_IN
            } else {
                _state.value = AuthStateEnum.LOGGED_OUT
            }
        }
    }

    fun clearAuth() {
        _authState.value = EmptyAuthState
        clearPersistedAuthState()
    }

    fun onNewAuthState(newState: AuthState) {
        GlobalScope.launch(dispatchers.main) {
            // Update our local state
            _authState.value = newState
        }
        GlobalScope.launch(dispatchers.io) {
            // Persist auth state
            persistAuthState(newState)
        }
    }

    private fun readAuthState(): AuthState {
        val stateJson = authPrefs.getString(PreferenceAuthKey, null)
        return when {
            stateJson != null -> AuthState.jsonDeserialize(stateJson)
            else -> AuthState(loading = false)
        }
    }

    private fun persistAuthState(state: AuthState) {
        authPrefs.edit(commit = true) {
            putString(PreferenceAuthKey, state.jsonSerializeString())
        }
    }

    private fun clearPersistedAuthState() {
        authPrefs.edit(commit = true) {
            remove(PreferenceAuthKey)
        }
    }

    fun onNewVersion(ultimaVersao: String) {
        GlobalScope.launch(dispatchers.main) {
            // Update our local state
            _ultimaVersao.value = ultimaVersao
        }
    }

    companion object {
        private val EmptyAuthState = AuthState(loading = true)
        private const val PreferenceAuthKey = "stateJson"
    }
}