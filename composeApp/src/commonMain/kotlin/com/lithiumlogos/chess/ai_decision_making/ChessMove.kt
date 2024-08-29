package com.lithiumlogos.chess.ai_decision_making

import androidx.compose.ui.unit.IntOffset
import com.lithiumlogos.chess.board.Board
import com.lithiumlogos.chess.pieces.Pawn
import com.lithiumlogos.chess.pieces.Piece
import kotlin.math.abs

data class ChessMove(
    val piece: Piece,
    val toSquare: IntOffset,
    val ambiguity: String = "",
    val isCapture: Boolean = false,
    val isCheck: Boolean = false
) {
    private val _notation = StringBuilder()
    val notation get() = _notation.toString()

    private val pNot = if (piece.type == 'P') "" else piece.type
    private val cX = toSquare.x.toChar().lowercase()
    private val cY = toSquare.y
    private val ck = if (isCheck) "+" else ""
    private val amb = if (pNot == "") {
        if (isCapture) {
            ambiguity.lowercase() + "x"
        } else { ambiguity }
    } else if (isCapture && ambiguity == "") "x" else ambiguity.lowercase()

    init {
        val diff = abs(piece.position.x - toSquare.x)
        if (piece.type == 'K' && diff > 1) {
            val kingSide = (piece.position.x - toSquare.x) < 0
            if (kingSide) {
                _notation.append("O-O")
            } else {
                _notation.append("O-O-O")
            }

        } else {
            _notation.append(pNot).append(amb).append(cX).append(cY).append(ck)
        }
    }
}

fun notationDecoder(notation: String, playerColor: Piece.Color, board: Board): ChessMove {
    val pieces = mutableListOf<Piece>()

    board.pieces.forEach { piece -> if (piece.color == playerColor) pieces += piece }

    var piece:Piece = Pawn(Piece.Color.Black, IntOffset.Zero)
    var toSquare = IntOffset.Zero
    var ambiguity = ""
    var isCapture = false
    var isCheck = false

    // Nfe4+ *ISH
    // e4 *DONE
    // exd5 *DONE
    // O-O for kingside and O-O-O for queenside. *DONE
    // fxe8=Q (Pawn promotes to Queen). *ISH

    // Castling

    if (notation == "O-O") {
        piece = pieces.find { it.type == 'K' }!!
        toSquare = IntOffset('G'.code, piece.position.y)
        return ChessMove(piece, toSquare)
    }

    if (notation == "O-O-O") {
        piece = pieces.find { it.type == 'K' }!!
        toSquare = IntOffset('C'.code, piece.position.y)
        return ChessMove(piece, toSquare)
    }

    // Pawns

    if (notation.first().isLowerCase()) {
//        if (notation[2] == '=' || notation[4] == '=') {
//            // TODO: Promotion...
//        }
        if (notation[1] == 'x') {
            val tempX = notation.uppercase().first()
            toSquare = getCoords(notation.substring(2, notation.length))
            val pawns = pieces.filter { it.type == 'P' && it.position.x == tempX.code }
            piece = pawns.find {
                it.getAvailableMoves(board.pieces, board.currentFEN)
                    .contains(toSquare)
            }!!
//            return ChessMove(piece, coords)
        } else {
            toSquare = getCoords(notation.substring(0, 2))
            piece = pieces.find {
                it.type == 'P' &&
                it.getAvailableMoves(board.pieces, board.currentFEN)
                    .contains(toSquare)
            }!!
//            return ChessMove(piece, coords)
        }
    } else {
        val nType = notation.first()
        if (notation[2].isDigit()) {
            toSquare = getCoords(notation.substring(1, 3))
            piece = pieces.find {
                it.type == nType &&
                it.getAvailableMoves(board.pieces, board.currentFEN)
                    .contains(toSquare)
            }!!
//            return ChessMove(piece, coords)
        } else {
            val coords = getCoords(notation.substring(2, 4))
            piece = pieces.find {
                it.type == nType &&
                        it.getAvailableMoves(board.pieces, board.currentFEN)
                            .contains(coords)
            }!!
        }
    }

    return ChessMove(piece, toSquare)
}

fun getCoords(coords: String): IntOffset {
    val work = coords.uppercase()
    val x = work[0].code
    val y = work[1].digitToInt()
    return IntOffset(x, y)
}