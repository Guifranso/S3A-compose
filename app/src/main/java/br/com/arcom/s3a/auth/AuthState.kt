package br.com.arcom.s3a.auth

import com.google.gson.Gson

enum class AuthStateEnum {
    LOGGED_IN, LOGGED_OUT, LOADING
}

class AuthState constructor(val nomeAcessor: String? = null,
                            val idSetor: Long?= null,
                            val loading: Boolean = false) {

    fun isAuthorized():Boolean {
        return idSetor != null && nomeAcessor != null
    }

    fun jsonSerializeString():String = Gson().toJson(this)

    companion object {
        @JvmStatic
        fun jsonDeserialize(value: String): AuthState =  Gson().fromJson(value, AuthState::class.java)
    }
}