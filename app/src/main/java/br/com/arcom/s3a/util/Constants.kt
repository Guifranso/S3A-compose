package br.com.arcom.s3a.util

const val DATABASE_NAME = "s3a-db"
val LOCALDATE_FORMATS = listOf("dd/MM/yyyy", "yyyy/MM/dd", "yy/MM/dd", "yyyy-MM-dd")
val LOCALDATETIME_FORMATS = listOf(
    "dd/MM/yyyy HH:mm:ss",
    "yyyy/MM/dd HH:mm:ss",
    "yyyy-MM-dd HH:mm:ss",
    "yyyy-MM-dd HH:mm:ss.SSS",
    "yyyy-MM-dd HH:mm:ss.SS",
    "yyyy-MM-dd HH:mm:ss.S",
    "dd-MM-yyyy HH:mm:ss",
    "dd-MM-yyyy HH:mm",
    "dd/MM/yyyy HH:mm",
    "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS"
)
const val LOCALDATE_PADRAO = "yyyy-MM-dd"
const val LOCALDATETIME_PADRAO = "yyyy-MM-dd HH:mm:ss"