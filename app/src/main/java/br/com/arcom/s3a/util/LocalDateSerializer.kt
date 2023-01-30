package br.com.arcom.s3a.util

import br.com.arcom.s3a.util.formatDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDate::class)
object LocalDateSerializer: KSerializer<LocalDate> {
    override fun deserialize(decoder: Decoder): LocalDate {
        val data = decoder.decodeString()
        val format = data.javaClass.annotations.find { it is DateFormat } as? DateFormat
        if( format != null ) {
            return data.formatDate( format.format )
        } else {
            LOCALDATE_FORMATS.forEach {
                try {
                    return data.formatDate(it)
                } catch (ex1: Throwable) {
                }
            }
        }
        throw RuntimeException("Formato de data nao configurado $data")
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "LocalDate",
        kind = PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val format = value.javaClass.annotations.find { it is DateFormat } as? DateFormat
        return if (format != null) {
            encoder.encodeString(value.formatData(format.format))
        } else {
            encoder.encodeString(value.formatData(LOCALDATE_PADRAO))
        }
    }
}