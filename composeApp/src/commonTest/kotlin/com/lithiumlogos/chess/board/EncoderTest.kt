package com.lithiumlogos.chess.board

import com.lithiumlogos.chess.pieces.Piece
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EncoderTest {
    @Test
    fun testDefaultEncoding() {
        val defaultBoard = Board("")
        val expected = DEFAULT_FEN_SETUP

        val defineTurn = Piece.Color.White
        val result = encode(defaultBoard.pieces, defineTurn, DEFAULT_FEN_SETUP)

        assertNotEquals(expected, defaultBoard.pieces)
        assertEquals(expected, result)
    }

    @Test
    fun testRandomEncoding() {
        // Sorta random.... each character means something now
        val fenString = "1nbqk3/4pppp/8/8/8/8/PPP3PP/1N2KB2 b - - 0 1"
        val randomBoard = Board(fenString)
        val expected = fenString

        val defineTurn = Piece.Color.Black
        val result = encode(randomBoard.pieces, defineTurn, DEFAULT_FEN_SETUP)

        assertNotEquals(expected, randomBoard.pieces)
        assertEquals(expected, result)
    }
}