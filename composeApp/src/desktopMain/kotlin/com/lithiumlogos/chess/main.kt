package com.lithiumlogos.chess

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ChessGame",
        state = WindowState(width = 713.dp, height = 800.dp)
    ) {
        App()
    }
}