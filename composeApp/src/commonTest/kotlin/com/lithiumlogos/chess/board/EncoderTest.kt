package com.lithiumlogos.chess.board

import com.lithiumlogos.chess.pieces.Piece
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EncoderTest {
    @Test
    fun testDefaultEncoding() {
        val defaultBoard = Board("")
        val expected = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w"

        val defineTurn = Piece.Color.White
        val result = encode(defaultBoard.pieces, defineTurn)

        assertNotEquals(expected, defaultBoard.pieces)
        assertEquals(expected, result)
    }

    @Test
    fun testRandomEncoding() {
        val fenString = "rnbqk3/4pppp/8/8/8/8/PPP3PP/RN2KB2 b"
        val randomBoard = Board(fenString)
        val expected = fenString

        val defineTurn = Piece.Color.Black
        val result = encode(randomBoard.pieces, defineTurn)

        assertNotEquals(expected, randomBoard.pieces)
        assertEquals(expected, result)
    }
}