package com.lithiumlogos.chess.board

import com.lithiumlogos.chess.pieces.Piece

fun encode(pieces: List<Piece>, playerTurn: Piece.Color): String {

    var initialList = mutableListOf(
        "XXXXXXXX",
        "XXXXXXXX",
        "XXXXXXXX",
        "XXXXXXXX",
        "XXXXXXXX",
        "XXXXXXXX",
        "XXXXXXXX",
        "XXXXXXXX"
    )

    pieces.forEach { piece ->
        val positionX = BoardXCoordinates.indexOf(piece.position.x)
        val positionY = BoardYCoordinates.indexOf(piece.position.y)
        val pieceType:String = if (piece.color.isWhite) piece.type.uppercase() else piece.type.lowercase()

        val sb = StringBuilder(initialList[positionY])
        sb.setCharAt(positionX, pieceType.toCharArray()[0])
        initialList[positionY] = sb.toString()
    }

    val fenString = StringBuilder("")
    val turnChar:Char = if (playerTurn.isWhite) 'w' else 'b'

    initialList.forEach { fenString.append("$it/") }
    fenString.setLength(fenString.length - 1)

    fenString.append(' ').append(turnChar)

    return cleanAndCount(fenString)
}

private fun cleanAndCount(fenString: StringBuilder): String {
    val result = fenString.toString().replace(Regex("X+")) {
        it.value.length.toString()
    }

    return result
}
