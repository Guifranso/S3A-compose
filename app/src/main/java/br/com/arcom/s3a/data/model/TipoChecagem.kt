package br.com.arcom.s3a.data.model

import br.com.arcom.s3a.R

enum class TipoChecagem(
    val id: Long,
    val descricao: Int,
) {
    INICIO(0, R.string.checagem_inicial), FINAL(1, R.string.checagem_final);

    companion object {
        fun getById(id: Long): TipoChecagem? = values().firstOrNull {
            it.id == id
        }
    }
}