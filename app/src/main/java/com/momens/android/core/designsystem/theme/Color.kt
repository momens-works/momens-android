package com.momens.android.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Primary
private val Primary100 = Color(0xFF3765EF)
private val Primary50 = Color(0xFF668CFF)
private val Primary10 = Color(0xFFF2F5FF)

// Point
private val PointRed = Color(0xFFFE8E8E)
private val PointPurple = Color(0xFF7E70FF)
private val PointYellow = Color(0xFFFFB854)
private val PointMint = Color(0xFF70D4CC)

// Black & White
private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF191919)

// Grayscale
private val Gray100 = Color(0xFFEDF0F4)
private val Gray200 = Color(0xFFD5DADF)
private val Gray300 = Color(0xFFBCC4CC)
private val Gray400 = Color(0xFF959CA3)
private val Gray500 = Color(0xFF788087)
private val Gray600 = Color(0xFF666E74)
private val Gray700 = Color(0xFF4A5156)
private val Gray800 = Color(0xFF36393C)
private val Gray900 = Color(0xFF202223)

// Background
private val NavGray = Color(0xFF666E74).copy(alpha = 0.8f)
private val UiBg = Color(0xFFEFF1F1)
private val UiBlackBg = Color(0xFF191919).copy(alpha = 0.6f)


@Immutable
data class MomensColors(

    // Primary
    val primary100: Color,
    val primary50: Color,
    val primary10: Color,

    // Point
    val pointRed: Color,
    val pointPurple: Color,
    val pointYellow: Color,
    val pointMint: Color,

    // B&W
    val white: Color,
    val black: Color,

    // Gray
    val gray100: Color,
    val gray200: Color,
    val gray300: Color,
    val gray400: Color,
    val gray500: Color,
    val gray600: Color,
    val gray700: Color,
    val gray800: Color,
    val gray900: Color,

    // Background
    val navGray: Color,
    val uiBg: Color,
    val uiBlackBg: Color
)

val defaultMomensColors = MomensColors(

    primary100 = Primary100,
    primary50 = Primary50,
    primary10 = Primary10,

    pointRed = PointRed,
    pointPurple = PointPurple,
    pointYellow = PointYellow,
    pointMint = PointMint,

    white = White,
    black = Black,

    navGray = NavGray,
    uiBg = UiBg,
    uiBlackBg = UiBlackBg,

    gray100 = Gray100,
    gray200 = Gray200,
    gray300 = Gray300,
    gray400 = Gray400,
    gray500 = Gray500,
    gray600 = Gray600,
    gray700 = Gray700,
    gray800 = Gray800,
    gray900 = Gray900,
)
