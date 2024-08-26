package com.lithiumlogos.chess.board

import androidx.compose.ui.unit.IntOffset
import com.lithiumlogos.chess.pieces.Bishop
import com.lithiumlogos.chess.pieces.King
import com.lithiumlogos.chess.pieces.Knight
import com.lithiumlogos.chess.pieces.Pawn
import com.lithiumlogos.chess.pieces.Piece
import com.lithiumlogos.chess.pieces.Queen
import com.lithiumlogos.chess.pieces.Rook

fun decode(fenString: String): List<Piece> {
    var pieces = mutableListOf<Piece>()
    // "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"

    var x = 0
    var y = 0

    for (c in fenString) {
        val color = if (c.isUpperCase()) Piece.Color.White else Piece.Color.Black
        val position = IntOffset(x = BoardXCoordinates[x], y = BoardYCoordinates[y])
        if (c.isLetter()) {
            when (c.uppercase()) {
                "R" -> pieces.add(Rook(color, position))
                "N" -> pieces.add(Knight(color,position))
                "B" -> pieces.add(Bishop(color, position))
                "Q" -> pieces.add(Queen(color, position))
                "K" -> pieces.add(King(color, position))
                "P" -> pieces.add(Pawn(color, position))
                else -> throw IllegalArgumentException("Invalid piece type: $c")
            }
            if (x < 7) x++
        }

        if (c.isDigit()) {
            x += c.digitToInt()
            if (x > 7) x--
        }

        if (c == '/') {
            x = 0
            y++
        }

        if (c == ' ') break
    }

    return pieces
}
