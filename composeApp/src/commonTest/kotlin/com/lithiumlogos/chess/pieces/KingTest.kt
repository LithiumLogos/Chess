package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset
import com.lithiumlogos.chess.board.DEFAULT_FEN_SETUP
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
        val movesWhite = whiteKing.getAvailableMoves(listOf(whiteKing), DEFAULT_FEN_SETUP)
        val movesBlack = blackKing.getAvailableMoves(listOf(blackKing), DEFAULT_FEN_SETUP)

        assertEquals(7, movesWhite.count())
        assertTrue(IntOffset(x = 'D'.code, y = 2) in movesWhite)
        assertTrue(IntOffset(x = 'F'.code, y = 1) in movesWhite)

        assertEquals(7, movesBlack.count())
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

        val movesWhite = whiteKing.getAvailableMoves(pieces, DEFAULT_FEN_SETUP)
        val movesBlack = blackKing.getAvailableMoves(pieces, DEFAULT_FEN_SETUP)

        assertTrue(movesWhite.isEmpty())
        assertTrue(movesBlack.isEmpty())
    }

    @Test
    fun testCantMoveOutOfBounds() {

        val movesWhite = whiteKing.getAvailableMoves(listOf(whiteKing), DEFAULT_FEN_SETUP)
        val movesBlack = blackKing.getAvailableMoves(listOf(blackKing), DEFAULT_FEN_SETUP)

        // Guess I'll just list out every move
        assertEquals(7, movesWhite.count())
        assertFalse(IntOffset(x = 'E'.code, y = 3) in movesWhite)

        assertTrue(IntOffset(x = 'D'.code, y = 1) in movesWhite)
        assertTrue(IntOffset(x = 'D'.code, y = 2) in movesWhite)
        assertTrue(IntOffset(x = 'E'.code, y = 2) in movesWhite)
        assertTrue(IntOffset(x = 'F'.code, y = 2) in movesWhite)
        assertTrue(IntOffset(x = 'F'.code, y = 1) in movesWhite)
        // Technically..
        assertTrue(IntOffset(x = 'G'.code, y = 1) in movesWhite)
        assertTrue(IntOffset(x = 'C'.code, y = 1) in movesWhite)

        assertEquals(7, movesBlack.count())
        assertFalse(IntOffset(x = 'E'.code, y = 6) in movesBlack)

        assertTrue(IntOffset(x = 'D'.code, y = 8) in movesBlack)
        assertTrue(IntOffset(x = 'D'.code, y = 7) in movesBlack)
        assertTrue(IntOffset(x = 'E'.code, y = 7) in movesBlack)
        assertTrue(IntOffset(x = 'F'.code, y = 7) in movesBlack)
        assertTrue(IntOffset(x = 'F'.code, y = 8) in movesBlack)
        // Technically..
        assertTrue(IntOffset(x = 'G'.code, y = 8) in movesBlack)
        assertTrue(IntOffset(x = 'C'.code, y = 8) in movesBlack)
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

        val movesWhite = whiteKing.getAvailableMoves(pieces, DEFAULT_FEN_SETUP)
        val movesBlack = blackKing.getAvailableMoves(pieces, DEFAULT_FEN_SETUP)

        assertTrue(movesWhite.contains(blackPawnEnemy.position))
        assertTrue(movesBlack.contains(whitePawnEnemy.position))
    }
}
