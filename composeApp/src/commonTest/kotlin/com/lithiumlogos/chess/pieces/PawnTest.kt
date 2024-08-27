package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PawnTest {
    private val whitePawn: Piece = Pawn(
        color = Piece.Color.White,
        position = IntOffset(x = 'A'.code, y = 2)
    )

    private val blackPawn: Piece = Pawn(
        color = Piece.Color.Black,
        position = IntOffset(x = 'A'.code, y = 7)
    )

    @Test
    fun testFirstMoveForward() {
        val movesWhite = whitePawn.getAvailableMoves(listOf(whitePawn), "")
        val movesBlack = blackPawn.getAvailableMoves(listOf(blackPawn), "")

        assertEquals(2, movesWhite.size)
        assertTrue(IntOffset(x = 'A'.code, y = 3) in movesWhite)
        assertTrue(IntOffset(x = 'A'.code, y = 4) in movesWhite)

        assertEquals(2, movesBlack.size)
        assertTrue(IntOffset(x = 'A'.code, y = 6) in movesBlack)
        assertTrue(IntOffset(x = 'A'.code, y = 5) in movesBlack)
    }

    @Test
    fun testSecondMoveForward() {
        whitePawn.position = IntOffset(x = 'A'.code, y = 3)
        blackPawn.position = IntOffset(x = 'A'.code, y = 6)

        val movesWhite = whitePawn.getAvailableMoves(listOf(whitePawn), "")
        val movesBlack = blackPawn.getAvailableMoves(listOf(blackPawn), "")

        assertEquals(1, movesWhite.size)
        assertEquals(IntOffset(x = 'A'.code, y = 4), movesWhite.first())

        assertEquals(1, movesBlack.size)
        assertEquals(IntOffset(x = 'A'.code, y = 5), movesBlack.first())
    }

    @Test
    fun testNoPossibleMoves() {
        val whitePawnBlocker = Pawn(
            color = Piece.Color.White,
            position = IntOffset(x = 'A'.code, y = 6)
        )

        val blackPawnBlocker = Pawn(
            color = Piece.Color.Black,
            position = IntOffset(x = 'A'.code, y = 3)
        )

        val pieces = listOf(
            whitePawn,
            blackPawnBlocker,
            blackPawn,
            whitePawnBlocker
        )

        val movesWhite = whitePawn.getAvailableMoves(pieces, "")
        val movesBlack = blackPawn.getAvailableMoves(pieces, "")

        assertTrue(movesWhite.isEmpty())
        assertTrue(movesBlack.isEmpty())
    }

    @Test
    fun testCantMoveOutOfBounds() {
        whitePawn.position = IntOffset(x = 'A'.code, y = 8)
        blackPawn.position = IntOffset(x = 'A'.code, y = 1)

        val movesWhite = whitePawn.getAvailableMoves(listOf(whitePawn), "")
        val movesBlack = blackPawn.getAvailableMoves(listOf(blackPawn), "")

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
            whitePawn,
            whitePawnEnemy,
            blackPawn,
            blackPawnEnemy
        )

        val movesWhite = whitePawn.getAvailableMoves(pieces, "")
        val movesBlack = blackPawn.getAvailableMoves(pieces, "")

        assertTrue(movesWhite.contains(blackPawnEnemy.position))
        assertTrue(movesBlack.contains(whitePawnEnemy.position))
    }
}
