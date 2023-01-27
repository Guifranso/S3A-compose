package br.com.arcom.s3a.data.model

import android.content.Context
import com.google.gson.Gson

data class Cronograma(
    val id: Long,
    val usuario: String,
    val dataInicio: String,
    val dataFim: String,
    val diaAtual: Int,
)
//{
//
//    fun save(context: Context) {
//        val sharedPreferences = context.getSharedPreferences("cronograma", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        val gson = Gson()
//        val json = gson.toJson(this)
//        editor.putString("cronograma", json)
//        editor.apply()
//    }
//
//    companion object {
//        fun getSavedCronograma(context: Context): Cronograma {
//            val sharedPreferences = context.getSharedPreferences("cronograma", Context.MODE_PRIVATE)
//            val json = sharedPreferences.getString("cronograma", "")
//            if (!json.isNullOrEmpty()) {
//                return Gson().fromJson(json, Cronograma::class.java)
//            }
//            return Cronograma(0L, "","","", 0)
//        }
//    }
//}