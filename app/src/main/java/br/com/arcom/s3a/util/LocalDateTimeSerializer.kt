package br.com.arcom.s3a.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer: KSerializer<LocalDateTime> {
    override fun deserialize(decoder: Decoder): LocalDateTime {
        val data = decoder.decodeString()
        val format = data.javaClass.annotations.find { it is DateFormat } as? DateFormat
        if( format != null ) {
            return data.formatDateTime( format.format )
        } else {
            LOCALDATETIME_FORMATS.forEach {
                try {
                    return data.formatDateTime(it)
                } catch (ex1: Throwable) {
                }
            }
        }
        throw RuntimeException("Formato de data nao configurado $data")
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "LocalDateTime",
        kind = PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val format = value.javaClass.annotations.find { it is DateFormat } as? DateFormat
        return if (format != null) {
            encoder.encodeString(value.formatData(format.format))
        } else {
            encoder.encodeString(value.formatData(LOCALDATETIME_PADRAO))
        }
    }
}