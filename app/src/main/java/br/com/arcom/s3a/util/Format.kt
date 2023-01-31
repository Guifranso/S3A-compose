package br.com.arcom.s3a.util

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun String.asNumber(): Boolean {
    return if (this.isNotEmpty()) {
        this.all { char -> char.isDigit() }
    }else{
        true
    }
}

fun String.getNumbers() = this.filter { it.isDigit() }.toIntOrNull()

fun Bitmap.toBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 75, byteArrayOutputStream)
    return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
}