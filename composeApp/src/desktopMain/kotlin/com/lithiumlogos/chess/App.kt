package com.lithiumlogos.chess

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.lithiumlogos.chess.ui.BoardUI
import com.lithiumlogos.chess.board.rememberBoard

@Composable
fun App() {
    val board = rememberBoard()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        BoardUI(
            board = board
        )
    }

}