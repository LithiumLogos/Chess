package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class QueenTest {
    private val whiteQueen: Piece = Queen(
        color = Piece.Color.White,
        position = IntOffset(x = 'D'.code, y = 1)
    )

    private val blackQueen: Piece = Queen(
        color = Piece.Color.Black,
        position = IntOffset(x = 'D'.code, y = 8)
    )

    @Test
    fun testFreeMovement() {
        val movesWhite = whiteQueen.getAvailableMoves(listOf(whiteQueen), "")
        val movesBlack = blackQueen.getAvailableMoves(listOf(blackQueen), "")

        assertEquals(21, movesWhite.size)
        assertTrue(IntOffset(x = 'A'.code, y = 4) in movesWhite)
        assertTrue(IntOffset(x = 'D'.code, y = 8) in movesWhite)

        assertEquals(21, movesBlack.size)
        assertTrue(IntOffset(x = 'A'.code, y = 5) in movesBlack)
        assertTrue(IntOffset(x = 'D'.code, y = 1) in movesBlack)
    }

    @Test
    fun testNoPossibleMoves() {
        val whitePawnOne = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'C'.code, y = 1)
        )

        val whitePawnTwo = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'C'.code, y = 2)
        )

        val whitePawnThree = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'D'.code, y = 2)
        )

        val whitePawnFour = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'E'.code, y = 2)
        )

        val whitePawnFive = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'E'.code, y = 1)
        )

        val blackPawnOne = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'C'.code, y = 8)
        )

        val blackPawnTwo = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'C'.code, y = 7)
        )

        val blackPawnThree = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'D'.code, y = 7)
        )

        val blackPawnFour = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'E'.code, y = 7)
        )

        val blackPawnFive = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'E'.code, y = 8)
        )

        val pieces = listOf(
            whiteQueen,
            whitePawnOne,
            whitePawnTwo,
            whitePawnThree,
            whitePawnFour,
            whitePawnFive,
            blackQueen,
            blackPawnOne,
            blackPawnTwo,
            blackPawnThree,
            blackPawnFour,
            blackPawnFive
        )

        val movesWhite = whiteQueen.getAvailableMoves(pieces, "")
        val movesBlack = blackQueen.getAvailableMoves(pieces, "")

        assertTrue(movesWhite.isEmpty())
        assertTrue(movesBlack.isEmpty())
    }

    @Test
    fun testCaptureEnemy() {
        val whitePawnEnemy = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'B'.code, y = 6)
        )

        val blackPawnEnemy = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'B'.code, y = 3)
        )

        val pieces = listOf(
            whiteQueen,
            whitePawnEnemy,
            blackQueen,
            blackPawnEnemy
        )

        val movesWhite = whiteQueen.getAvailableMoves(pieces, "")
        val movesBlack = blackQueen.getAvailableMoves(pieces, "")

        assertTrue(movesWhite.contains(blackPawnEnemy.position))
        assertTrue(movesBlack.contains(whitePawnEnemy.position))
    }
}
