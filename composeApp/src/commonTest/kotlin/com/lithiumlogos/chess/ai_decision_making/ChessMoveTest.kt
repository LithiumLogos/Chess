package com.lithiumlogos.chess.ai_decision_making

import androidx.compose.ui.unit.IntOffset
import com.lithiumlogos.chess.board.Board
import com.lithiumlogos.chess.board.DEFAULT_FEN_SETUP
import com.lithiumlogos.chess.pieces.Bishop
import com.lithiumlogos.chess.pieces.King
import com.lithiumlogos.chess.pieces.Knight
import com.lithiumlogos.chess.pieces.Pawn
import com.lithiumlogos.chess.pieces.Piece
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ChessMoveTest {
    private val defaultWhitePawn = Pawn(Piece.Color.White, IntOffset('E'.code, 2))
    private val defaultWhiteKnight = Knight(Piece.Color.White, IntOffset('B'.code, 1))
    private val pawnMoveD4 = ChessMove(defaultWhitePawn, IntOffset('E'.code, 4))

    @Test
    fun testBasicMove() {
        val expectedMove = IntOffset('E'.code, 4)
        val expectedNotation = "e4"

        val resultPiece = pawnMoveD4.piece
        val resultMove = pawnMoveD4.toSquare
        val resultNotation = pawnMoveD4.notation

        assertEquals(defaultWhitePawn, resultPiece)
        assertEquals(expectedMove, resultMove)
        assertEquals(expectedNotation, resultNotation)
    }

    @Test
    fun testBasicNotation() {
        val knight2 = Knight(Piece.Color.White, IntOffset('G'.code, 1))
        val pawn2 = Pawn(Piece.Color.White, IntOffset('F'.code, 2))
        val king1 = King(Piece.Color.White, IntOffset('E'.code, 1))
        val expected = setOf(
            "e4",
            "Nc3",
            "Nh3",
            "f3",
            "O-O",
            "O-O-O"
        )
        val p1m = ChessMove(defaultWhitePawn, IntOffset('E'.code, 4))
        val n1m = ChessMove(defaultWhiteKnight, IntOffset('C'.code, 3))
        val n2m = ChessMove(knight2, IntOffset('H'.code, 3))
        val p2m = ChessMove(pawn2, IntOffset('F'.code, 3))
        val k1m = ChessMove(king1, IntOffset('G'.code, 1))
        val k2m = ChessMove(king1, IntOffset('C'.code, 1))

        val result = setOf(
            p1m.notation,
            n1m.notation,
            n2m.notation,
            p2m.notation,
            k1m.notation,
            k2m.notation,
        )

        assertEquals(expected, result)
    }

    @Test
    fun testMoreNotation() {
        val bishop = Bishop(Piece.Color.White, IntOffset('G'.code, 1))
        val expected = setOf(
            "exd4",
            "Nfc3",
            "Bxb1",
        )

        val p1m = ChessMove(defaultWhitePawn, IntOffset('D'.code, 4), "E", isCapture = true)
        val n1m = ChessMove(defaultWhiteKnight, IntOffset('C'.code, 3), "f")
        val b1m = ChessMove(bishop, IntOffset('B'.code, 1), isCapture = true)

        val result = setOf(
            p1m.notation,
            n1m.notation,
            b1m.notation,
        )

        assertEquals(expected, result)
    }

    @Test
    fun testDefaultDecoder() {
        val defaultBoard = Board(DEFAULT_FEN_SETUP)
        val expectedPawn = defaultBoard.pieces.find { it.position == IntOffset('E'.code, 2) }!!
        val expectedKnight = defaultBoard.pieces.find { it.position == IntOffset('B'.code, 1) }!!
        val expectedIntP = IntOffset('E'.code, 4)
        val expectedIntN = IntOffset('C'.code, 3)
        val expectedMoveP = ChessMove(expectedPawn, expectedIntP)
        val expectedMoveN = ChessMove(expectedKnight, expectedIntN)

        val resultOne = notationDecoder("e4", Piece.Color.White, defaultBoard)
        val resultTwo = notationDecoder("Nc3", Piece.Color.White, defaultBoard)

        assertEquals(expectedMoveP, resultOne)
        assertEquals(expectedPawn, resultOne.piece)
        assertEquals(expectedIntP, resultOne.toSquare)
        assertEquals("e4", resultOne.notation)

        assertEquals(expectedMoveN, resultTwo)
        assertEquals(expectedKnight, resultTwo.piece)
        assertEquals(expectedIntN, resultTwo.toSquare)
        assertEquals("Nc3", resultTwo.notation)
    }
}