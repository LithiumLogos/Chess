package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KnightTest {
    private val whiteKnight: Piece = Knight(
        color = Piece.Color.White,
        position = IntOffset(x = 'B'.code, y = 1)
    )

    private val blackKnight: Piece = Knight(
        color = Piece.Color.Black,
        position = IntOffset(x = 'B'.code, y = 8)
    )

    @Test
    fun testFreeMovement() {
        val movesWhite = whiteKnight.getAvailableMoves(listOf(whiteKnight))
        val movesBlack = blackKnight.getAvailableMoves(listOf(blackKnight))

        assertEquals(3, movesWhite.size)
        assertTrue(IntOffset(x = 'A'.code, y = 3) in movesWhite)
        assertTrue(IntOffset(x = 'C'.code, y = 3) in movesWhite)
        assertTrue(IntOffset(x = 'D'.code, y = 2) in movesWhite)

        assertEquals(3, movesBlack.size)
        assertTrue(IntOffset(x = 'A'.code, y = 6) in movesBlack)
        assertTrue(IntOffset(x = 'C'.code, y = 6) in movesBlack)
        assertTrue(IntOffset(x = 'D'.code, y = 7) in movesBlack)
    }

    @Test
    fun testNoPossibleMoves() {
        val whitePawnOne = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'A'.code, y = 3)
        )

        val whitePawnTwo = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'C'.code, y = 3)
        )

        val whitePawnThree = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'D'.code, y = 2)
        )

        val blackPawnOne = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'A'.code, y = 6)
        )

        val blackPawnTwo = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'C'.code, y = 6)
        )

        val blackPawnThree = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'D'.code, y = 7)
        )

        val pieces = listOf(
            whiteKnight,
            whitePawnOne,
            whitePawnTwo,
            whitePawnThree,
            blackKnight,
            blackPawnOne,
            blackPawnTwo,
            blackPawnThree
        )

        val movesWhite = whiteKnight.getAvailableMoves(pieces)
        val movesBlack = blackKnight.getAvailableMoves(pieces)

        assertTrue(movesWhite.isEmpty())
        assertTrue(movesBlack.isEmpty())
    }

    // testCantMoveOutOfBounds completed in testFreeMovement

    @Test
    fun testCaptureEnemy() {
        val whitePawnEnemy = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'A'.code, y = 6)
        )

        val blackPawnEnemy = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'A'.code, y = 3)
        )

        val pieces = listOf(
            whiteKnight,
            whitePawnEnemy,
            blackKnight,
            blackPawnEnemy
        )

        val movesWhite = whiteKnight.getAvailableMoves(pieces)
        val movesBlack = blackKnight.getAvailableMoves(pieces)

        assertTrue(movesWhite.contains(blackPawnEnemy.position))
        assertTrue(movesBlack.contains(whitePawnEnemy.position))
    }
}
