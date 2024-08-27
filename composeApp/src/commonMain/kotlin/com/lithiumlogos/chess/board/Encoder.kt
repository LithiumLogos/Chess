package com.lithiumlogos.chess.board

import androidx.compose.ui.unit.IntOffset
import com.lithiumlogos.chess.pieces.Piece

fun encode(pieces: List<Piece>, playerTurn: Piece.Color, previousState: String): String {

    // Piece Block
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

    initialList.forEach { fenString.append("$it/") }
    fenString.setLength(fenString.length - 1)

    // Turn Block
    val turnChar:Char = if (playerTurn.isWhite) 'w' else 'b'
    fenString.append(' ').append(turnChar)

    // Castle BLock
    val castleString = castleBlock(pieces, previousState)
    fenString.append(' ').append(castleString)

    // TODO: En Passant and Turn's Remaining
    fenString.append(" - 0 1")

    // An empty boardCell is shown as X until cleaned
    return cleanAndCount(fenString)
}


private fun cleanAndCount(fenString: StringBuilder): String {
    val result = fenString.toString().replace(Regex("X+")) {
        it.value.length.toString()
    }

    return result
}

private fun castleBlock(pieces: List<Piece>, previousState: String): String {
    val whiteBlock = StringBuilder("")
    val blackBlock = StringBuilder("")
    var whiteKing = pieces.find { it.type == 'K' && it.color.isWhite && !it.hasMoved }
    var blackKing = pieces.find { it.type == 'K' && it.color.isBlack && !it.hasMoved }
    var whiteKingRook = pieces.find { it.type == 'R' && it.color.isWhite && !it.hasMoved && it.position == IntOffset('H'.code, 1)}
    var whiteQueenRook = pieces.find { it.type == 'R' && it.color.isWhite && !it.hasMoved && it.position == IntOffset('A'.code, 1)}
    var blackKingRook = pieces.find { it.type == 'R' && it.color.isBlack  && !it.hasMoved && it.position == IntOffset('H'.code, 8)}
    var blackQueenRook = pieces.find { it.type == 'R' && it.color.isBlack && !it.hasMoved && it.position == IntOffset('A'.code, 8)}

    val previousString = previousState.split(' ')[2]
    if (previousString.contains('-')) return "-"

    whiteKing = if (previousString.contains('K') || previousString.contains('Q')) whiteKing else null
    blackKing = if (previousString.contains('k') || previousString.contains('q')) blackKing else null
    whiteKingRook = if (previousString.contains('K')) whiteKingRook else null
    whiteQueenRook = if (previousString.contains('Q')) whiteQueenRook else null
    blackKingRook = if (previousString.contains('k')) blackKingRook else null
    blackQueenRook = if (previousString.contains('q')) blackQueenRook else null

    if (whiteKing == null && blackKing == null) {
        return "-"
    }

    if (whiteKingRook != null) {
        whiteBlock.append('K')
    }

    if (whiteQueenRook != null) {
        whiteBlock.append('Q')
    }

    if (blackKingRook != null) {
        blackBlock.append('k')
    }

    if (blackQueenRook != null) {
        blackBlock.append('q')
    }

    if (whiteKing == null) {
        whiteBlock.clear()
    }

    if (blackKing == null) {
        blackBlock.clear()
    }

    val result = whiteBlock.append(blackBlock).toString()

    return result.ifEmpty { "-" }
}
