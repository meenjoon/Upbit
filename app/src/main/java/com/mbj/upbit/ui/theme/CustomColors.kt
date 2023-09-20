package com.mbj.upbit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class CustomColors {
    companion object {
        private val darkTheme: Boolean
            @Composable
            get() = isSystemInDarkTheme()

        val Purple80 = Color(0xFFD0BCFF)
        val PurpleGrey80 = Color(0xFFCCC2DC)
        val Pink80 = Color(0xFFEFB8C8)

        val Purple40 = Color(0xFF6650a4)
        val PurpleGrey40 = Color(0xFF625b71)
        val Pink40 = Color(0xFF7D5260)

        val Purple200 = Color(0xFFBB86FC)
        val Purple500 = Color(0xFF6200EE)
        val Purple700 = Color(0xFF3700B3)
        val Teal200 = Color(0xFF03DAC5)
        val Orange700 = Color(0xFFF33527)
        val Blue300 = Color(0xFF4F47D8)
        val Navy300 = Color(0xFF21202B)
        val Grey100 = Color(0xB4F0EEFF)
        val Grey500 = Color(0xFF8D8383)
        val zeroChangeTextColor: Color
            @Composable
            get() = if (darkTheme) Color.White else Color.Black
        val zeroChangeBoxColor: Color
            @Composable
            get() = if (darkTheme) Navy300 else Grey100
    }
}
