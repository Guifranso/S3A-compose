package br.com.arcom.s3a.ui.theme

import androidx.compose.ui.graphics.Color

internal val PrimaryColorLight = Color(0xFF65E096)
internal val OnPrimaryColorLight = Color(0xFFFFFFFF)
internal val PrimaryColorContainerLight = Color(0xFFFA7973)
internal val OnPrimaryColorContainerLight = Color(0xFF560E0E)
internal val SecondaryColorLight = Color(0xFFF8F7F7)
internal val OnSecondaryColorLight = Color(0xFF000000)
internal val SecondaryColorContainerLight = Color(0xFFc7c7c7)
internal val OnSecondaryColorContainerLight = Color(0xFF575757)
internal val ErrorColorLight = Color(0xFFCC3229)
internal val OnErrorColorLight = Color(0xFFFFFFFF)
internal val ErrorColorContainerLight = Color(0xFFF9DEDC)
internal val OnErrorColorContainerLight = Color(0xFF410E0B)
internal val BackgroundColorLight = Color(0xFFEBECED)
internal val OnBackgroundColorLight = Color(0xFF1C1B1F)
internal val SurfaceColorLight = Color(0xFFE4E4E4)
internal val OnSurfaceColorLight = Color(0xFF1C1B1F)

internal val PrimaryColorDark = Color(0xFF82D3A2)
internal val OnPrimaryColorDark = Color(0xFFFFFFFF)
internal val PrimaryColorContainerDark = Color(0xFF880E0E)
internal val OnPrimaryColorContainerDark = Color(0xFFF24F47)
internal val SecondaryColorDark = Color(0xFF333333)
internal val OnSecondaryColorDark = Color(0xFFFFFFFF)
internal val SecondaryColorContainerDark = Color(0xFFA39A9A)
internal val OnSecondaryColorContainerDark = Color(0xFF202020)
internal val ErrorColorDark = Color(0xFFD83C34)
internal val OnErrorColorDark = Color(0xFF601410)
internal val ErrorColorContainerDark = Color(0xFF8C1D18)
internal val OnErrorColorContainerDark = Color(0xFFF9DEDC)
internal val BackgroundColorDark = Color(0xFF1C1B1F)
internal val OnBackgroundColorDark = Color(0xFFFFFFFF)
internal val SurfaceColorDark = Color(0xFF383838)
internal val OnSurfaceColorDark = Color(0xFFFFFFFF)

fun Color.divider(): Color { return this.copy(0.2f) }
fun Color.textoSecundario(): Color { return this.copy(0.6f) }
fun Color.lightColor(): Color { return this.copy(0.3f) }