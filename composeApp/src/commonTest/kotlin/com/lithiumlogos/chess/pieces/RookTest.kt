package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RookTest {
    private val whiteRook: Piece = Rook(
        color = Piece.Color.White,
        position = IntOffset(x = 'A'.code, y = 1)
    )

    private val blackRook: Piece = Rook(
        color = Piece.Color.Black,
        position = IntOffset(x = 'A'.code, y = 8)
    )

    @Test
    fun testFreeMovement() {
        val movesWhite = whiteRook.getAvailableMoves(listOf(whiteRook), "")
        val movesBlack = blackRook.getAvailableMoves(listOf(blackRook), "")

        assertEquals(14, movesWhite.size)
        assertTrue(IntOffset(x = 'A'.code, y = 8) in movesWhite)
        assertTrue(IntOffset(x = 'H'.code, y = 1) in movesWhite)

        assertEquals(14, movesBlack.size)
        assertTrue(IntOffset(x = 'A'.code, y = 1) in movesBlack)
        assertTrue(IntOffset(x = 'H'.code, y = 8) in movesBlack)
    }

    @Test
    fun testNoPossibleMoves() {
        val whitePawnOne = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'A'.code, y = 2)
        )

        val whitePawnTwo = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'B'.code, y = 1)
        )

        val blackPawnOne = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'A'.code, y = 7)
        )

        val blackPawnTwo = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'B'.code, y = 8)
        )

        val pieces = listOf(
            whiteRook,
            whitePawnOne,
            whitePawnTwo,
            blackRook,
            blackPawnOne,
            blackPawnTwo
        )

        val movesWhite = whiteRook.getAvailableMoves(pieces, "")
        val movesBlack = blackRook.getAvailableMoves(pieces, "")

        assertTrue(movesWhite.isEmpty())
        assertTrue(movesBlack.isEmpty())
    }

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
            whiteRook,
            whitePawnEnemy,
            blackRook,
            blackPawnEnemy
        )

        val movesWhite = whiteRook.getAvailableMoves(pieces, "")
        val movesBlack = blackRook.getAvailableMoves(pieces, "")

        assertTrue(movesWhite.contains(blackPawnEnemy.position))
        assertTrue(movesBlack.contains(whitePawnEnemy.position))
    }
}
