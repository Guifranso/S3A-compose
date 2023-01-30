package br.com.arcom.s3a.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.com.arcom.s3a.R

val Poppins = FontFamily(
    Font(R.font.poppins_light, weight = FontWeight.Light),
    Font(R.font.poppins_regular, weight = FontWeight.Normal),
    Font(R.font.poppins_medium, weight = FontWeight.Medium),
    Font(R.font.poppins_semibold, weight = FontWeight.SemiBold),
    Font(R.font.poppins_bold, weight = FontWeight.Bold)
)

val S3aTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 57.sp,
        fontFamily = Poppins,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 45.sp,
        fontFamily = Poppins,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 36.sp,
        fontFamily = Poppins,
        lineHeight = 44.sp
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 32.sp,
        fontFamily = Poppins,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 28.sp,
        fontFamily = Poppins,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 24.sp,
        fontFamily = Poppins,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 22.sp,
        fontFamily = Poppins,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 16.sp,
        fontFamily = Poppins,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        fontFamily = Poppins,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        fontFamily = Poppins,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        fontFamily = Poppins,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        fontFamily = Poppins,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        fontFamily = Poppins,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        fontFamily = Poppins,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 10.sp,
        fontFamily = Poppins,
        lineHeight = 16.sp
    )
)