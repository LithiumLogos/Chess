package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset
import com.lithiumlogos.chess.board.BoardYCoordinates
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BishopTest {
    private val whiteBishop: Piece = Bishop(
        color = Piece.Color.White,
        position = IntOffset(x = 'C'.code, y = 1)
    )

    private val blackBishop: Piece = Bishop(
        color = Piece.Color.Black,
        position = IntOffset(x = 'C'.code, y = 8)
    )

    @Test
    fun random() {
        val eight = BoardYCoordinates[0]
        val seven = BoardYCoordinates[1]

        assertTrue(eight == 1)
        assertTrue(seven == 7)
    }

    @Test
    fun testFreeMovement() {
        val movesWhite = whiteBishop.getAvailableMoves(listOf(whiteBishop))
        val movesBlack = blackBishop.getAvailableMoves(listOf(blackBishop))

        assertEquals(7, movesWhite.size)
        assertTrue(IntOffset(x = 'A'.code, y = 3) in movesWhite)
        assertTrue(IntOffset(x = 'H'.code, y = 6) in movesWhite)

        assertEquals(7, movesBlack.size)
        assertTrue(IntOffset(x = 'A'.code, y = 6) in movesBlack)
        assertTrue(IntOffset(x = 'H'.code, y = 3) in movesBlack)
    }

    @Test
    fun testNoPossibleMoves() {
        val whitePawnOne = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'B'.code, y = 2)
        )

        val whitePawnTwo = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'D'.code, y = 2)
        )

        val blackPawnOne = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'B'.code, y = 7)
        )

        val blackPawnTwo = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'D'.code, y = 7)
        )

        val pieces = listOf(
            whiteBishop,
            whitePawnOne,
            whitePawnTwo,
            blackBishop,
            blackPawnOne,
            blackPawnTwo
        )

        val movesWhite = whiteBishop.getAvailableMoves(pieces)
        val movesBlack = blackBishop.getAvailableMoves(pieces)

        assertTrue(movesWhite.isEmpty())
        assertTrue(movesBlack.isEmpty())
    }

    @Test
    fun testCantMoveOutOfBounds() {

        val movesWhite = whiteBishop.getAvailableMoves(listOf(whiteBishop))
        val movesBlack = blackBishop.getAvailableMoves(listOf(blackBishop))

        // Guess I'll just list out every move
        assertEquals(7, movesWhite.size)
        assertFalse(IntOffset(x = 'C'.code, y = 2) in movesWhite)
        assertTrue(IntOffset(x = 'B'.code, y = 2) in movesWhite)
        assertTrue(IntOffset(x = 'A'.code, y = 3) in movesWhite)
        assertTrue(IntOffset(x = 'D'.code, y = 2) in movesWhite)
        assertTrue(IntOffset(x = 'E'.code, y = 3) in movesWhite)
        assertTrue(IntOffset(x = 'F'.code, y = 4) in movesWhite)
        assertTrue(IntOffset(x = 'G'.code, y = 5) in movesWhite)
        assertTrue(IntOffset(x = 'H'.code, y = 6) in movesWhite)

        assertEquals(7, movesBlack.size)
        assertFalse(IntOffset(x = 'C'.code, y = 7) in movesBlack)
        assertTrue(IntOffset(x = 'B'.code, y = 7) in movesBlack)
        assertTrue(IntOffset(x = 'A'.code, y = 6) in movesBlack)
        assertTrue(IntOffset(x = 'D'.code, y = 7) in movesBlack)
        assertTrue(IntOffset(x = 'E'.code, y = 6) in movesBlack)
        assertTrue(IntOffset(x = 'F'.code, y = 5) in movesBlack)
        assertTrue(IntOffset(x = 'G'.code, y = 4) in movesBlack)
        assertTrue(IntOffset(x = 'H'.code, y = 3) in movesBlack)
    }

    @Test
    fun testCaptureEnemy() {
        val whitePawnEnemy = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'B'.code, y = 7)
        )

        val blackPawnEnemy = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'B'.code, y = 2)
        )

        val pieces = listOf(
            whiteBishop,
            whitePawnEnemy,
            blackBishop,
            blackPawnEnemy
        )

        val movesWhite = whiteBishop.getAvailableMoves(pieces)
        val movesBlack = blackBishop.getAvailableMoves(pieces)

        assertTrue(movesWhite.contains(blackPawnEnemy.position))
        assertTrue(movesBlack.contains(whitePawnEnemy.position))
    }
}
