package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

import org.junit.jupiter.api.Test

class KingTest {
    private val whiteKing: Piece = King(
        color = Piece.Color.White,
        position = IntOffset(x = 'E'.code, y = 1)
    )

    private val blackKing: Piece = King(
        color = Piece.Color.Black,
        position = IntOffset(x = 'E'.code, y = 8)
    )

    @Test
    fun testFreeMovement() {
        val movesWhite = whiteKing.getAvailableMoves(listOf(whiteKing))
        val movesBlack = blackKing.getAvailableMoves(listOf(blackKing))

        assertEquals(5, movesWhite.size)
        assertTrue(IntOffset(x = 'D'.code, y = 2) in movesWhite)
        assertTrue(IntOffset(x = 'F'.code, y = 1) in movesWhite)

        assertEquals(5, movesBlack.size)
        assertTrue(IntOffset(x = 'F'.code, y = 7) in movesBlack)
        assertTrue(IntOffset(x = 'D'.code, y = 8) in movesBlack)
    }

    @Test
    fun testNoPossibleMoves() {
        val whitePawnOne = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'D'.code, y = 1)
        )

        val whitePawnTwo = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'D'.code, y = 2)
        )

        val whitePawnThree = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'E'.code, y = 2)
        )

        val whitePawnFour = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'F'.code, y = 2)
        )

        val whitePawnFive = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'F'.code, y = 1)
        )

        val blackPawnOne = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'D'.code, y = 8)
        )

        val blackPawnTwo = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'D'.code, y = 7)
        )

        val blackPawnThree = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'E'.code, y = 7)
        )

        val blackPawnFour = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'F'.code, y = 7)
        )

        val blackPawnFive = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'F'.code, y = 8)
        )

        val pieces = listOf(
            whiteKing,
            whitePawnOne,
            whitePawnTwo,
            whitePawnThree,
            whitePawnFour,
            whitePawnFive,
            blackKing,
            blackPawnOne,
            blackPawnTwo,
            blackPawnThree,
            blackPawnFour,
            blackPawnFive
        )

        val movesWhite = whiteKing.getAvailableMoves(pieces)
        val movesBlack = blackKing.getAvailableMoves(pieces)

        assertTrue(movesWhite.isEmpty())
        assertTrue(movesBlack.isEmpty())
    }

    @Test
    fun testCantMoveOutOfBounds() {

        val movesWhite = whiteKing.getAvailableMoves(listOf(whiteKing))
        val movesBlack = blackKing.getAvailableMoves(listOf(blackKing))

        // Guess I'll just list out every move
        assertEquals(5, movesWhite.size)
        assertFalse(IntOffset(x = 'E'.code, y = 3) in movesWhite)
        assertTrue(IntOffset(x = 'D'.code, y = 1) in movesWhite)
        assertTrue(IntOffset(x = 'D'.code, y = 2) in movesWhite)
        assertTrue(IntOffset(x = 'E'.code, y = 2) in movesWhite)
        assertTrue(IntOffset(x = 'F'.code, y = 2) in movesWhite)
        assertTrue(IntOffset(x = 'F'.code, y = 1) in movesWhite)

        assertEquals(5, movesBlack.size)
        assertFalse(IntOffset(x = 'E'.code, y = 6) in movesBlack)
        assertTrue(IntOffset(x = 'D'.code, y = 8) in movesBlack)
        assertTrue(IntOffset(x = 'D'.code, y = 7) in movesBlack)
        assertTrue(IntOffset(x = 'E'.code, y = 7) in movesBlack)
        assertTrue(IntOffset(x = 'F'.code, y = 7) in movesBlack)
        assertTrue(IntOffset(x = 'F'.code, y = 8) in movesBlack)
    }

    @Test
    fun testCaptureEnemy() {
        val whitePawnEnemy = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'D'.code, y = 7)
        )

        val blackPawnEnemy = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'D'.code, y = 2)
        )

        val pieces = listOf(
            whiteKing,
            whitePawnEnemy,
            blackKing,
            blackPawnEnemy
        )

        val movesWhite = whiteKing.getAvailableMoves(pieces)
        val movesBlack = blackKing.getAvailableMoves(pieces)

        assertTrue(movesWhite.contains(blackPawnEnemy.position))
        assertTrue(movesBlack.contains(whitePawnEnemy.position))
    }
}
