package com.lithiumlogos.chess.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lithiumlogos.chess.board.Board
import com.lithiumlogos.chess.board.BoardXCoordinates
import com.lithiumlogos.chess.board.BoardYCoordinates
import com.lithiumlogos.chess.board.rememberIsAvailableMove
import com.lithiumlogos.chess.board.rememberPieceAt

class BoardScreen(var board: Board, val modifier: Modifier = Modifier) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val currentTurn = board.currentFEN.split(' ')[1]
        val colorToMove = if (currentTurn == "w") "White" else "Black"
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    title = {
                        Text("$colorToMove's move",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Exit"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { navigator.push(HomeScreen(board.currentFEN)) }) {
                            Icon(imageVector = Icons.Outlined.Lock, contentDescription = "Copy FEN")
                        }
                    },
                )
            }
        ) {
            TextField(
                value = board.currentFEN,
                label = { Text("FEN String") },
                readOnly = true,
                onValueChange = { }
            )
            Column(
                modifier = modifier
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .border(
                        width = 8.dp,
                        color = MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(8.dp)
            ) {
                BoardYCoordinates.forEach { y ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        BoardXCoordinates.forEach { x ->
                            val piece = board.rememberPieceAt(x, y)
                            val isAvailableMove = board.rememberIsAvailableMove(x, y)
                            BoardCell(
                                x = x,
                                y = y,
                                piece = piece,
                                board = board,
                                isAvailableMove = isAvailableMove,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                    }
                }
            }
        }
    }
}
