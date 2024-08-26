package com.lithiumlogos.chess.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.lithiumlogos.chess.board.Board
import com.lithiumlogos.chess.board.BoardXCoordinates
import com.lithiumlogos.chess.pieces.Piece
import org.jetbrains.compose.resources.painterResource

@Composable
fun BoardCell(
    x:Int,
    y:Int,
    piece: Piece?,
    board: Board,
    isAvailableMove: Boolean,
    modifier: Modifier
) {
    val backgroundColor =
        when {
            piece != null && piece == board.selectedPiece -> ActiveColor
            (x + y) % 2 == 0 -> DarkColor
            else -> LightColor
        }

    val textColor =
        when {
            piece != null && piece == board.selectedPiece -> Color.White
            (x + y) % 2 == 0 -> LightColor
            else -> DarkColor
        }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
    ) {
        if (x == BoardXCoordinates.first()) {
            Text(
                text = y.toString(),
                color = textColor,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            )
        }

        if (y == 1) {
            Text(
                text = x.toChar().toString(),
                color = textColor,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            )
        }

        piece?.let {
            Image(
                painter = painterResource(it.drawable),
                contentDescription = null,
                modifier = Modifier
                    .clickable(interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        board.selectPiece(it)
                    }
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }

        if (isAvailableMove) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .clickable(interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        board.moveSelectedPiece(x = x, y = y)
                    }
                    .drawBehind {
                        drawCircle(
                            color = ActiveColor,
                            radius = size.width / 6f,
                            center = center,
                        )
                    }

            )
        }
    }
}
