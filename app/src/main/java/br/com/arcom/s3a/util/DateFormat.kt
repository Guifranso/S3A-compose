package br.com.arcom.s3a.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class DateFormat(val format: String)

@Throws(DateTimeParseException::class)
fun String.formatDate(format: String): LocalDate {
    try {
        return LocalDate.parse(this, DateTimeFormatter.ofPattern(format))
    }catch (e: Exception){
        throw Exception("Erro na formatação da data!")
    }
}

@Throws(DateTimeParseException::class)
fun String.formatDateTime(format: String): LocalDateTime {
    try {
        return LocalDateTime.parse(this, DateTimeFormatter.ofPattern(format))
    }catch (e: Exception){
        throw Exception("Erro na formatação da data!")
    }
}

fun LocalDateTime.formatData(format: String) = this.format(DateTimeFormatter.ofPattern(format))
fun LocalDate.formatData(format: String) = this.format(DateTimeFormatter.ofPattern(format))

