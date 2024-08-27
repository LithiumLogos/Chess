package com.lithiumlogos.chess.board

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntOffset
import com.lithiumlogos.chess.pieces.Piece
import com.lithiumlogos.chess.pieces.convertOffset
import kotlin.math.abs

@Immutable
class Board(val fenString: String) {
    private val _pieces = mutableStateListOf<Piece>()
    val pieces get() = _pieces.toList()
    val setup = fenString.ifBlank { DEFAULT_FEN_SETUP }

    init {
        decode(setup).forEach { piece -> _pieces.add(piece) }
    }

    var selectedPiece by mutableStateOf<Piece?>(null)
        private set

    var selectedPieceMoves by mutableStateOf(emptySet<IntOffset>())
        private set

    var moveIncrementAction by mutableIntStateOf(0)
        private set

    var playerTurn by mutableStateOf(getCurrentTurn(setup))


    var currentFEN by mutableStateOf(encode(pieces, playerTurn, setup))

    var enPassantTarget by mutableStateOf(IntOffset(0, 0))

    /*
        User Events
     */

    fun selectPiece(piece: Piece) {
        if (piece.color !== playerTurn) {
            return
        }
        if (piece == selectedPiece) {
            clearSelection()
        } else {
            selectedPiece = piece
            selectedPieceMoves = piece.getAvailableMoves(pieces = pieces, fenString = currentFEN)

        }
    }

    fun moveSelectedPiece(x:Int , y:Int ) {
        selectedPiece?.let { piece ->
            var enPassant = false
            var enY = 1
            if (!isAvailableMove(x = x, y = y)) {
                return
            }

            if (piece.color !== playerTurn) {
                return
            }

            if (piece.type == 'P') {
                val currentY = piece.position.y
                if (abs(currentY - y) == 2) {
                    enPassant = true
                    enY = (y + currentY) / 2
                    enPassantTarget = IntOffset(x,y)
                }
            }

            movePiece(piece = piece, position = IntOffset(x, y))
            moveRookCastle(piece = piece, position = IntOffset(x, y))
            clearSelection()
            switchPlayerTurn(currentFEN)
            updateFenString(pieces, playerTurn, enPassant, IntOffset(x, enY))
            moveIncrementAction++

        }
    }

    private fun moveRookCastle(piece: Piece, position: IntOffset) {
        if (piece.type != 'K') {
            return
        }

        val color = piece.color
        val rookCastle: Piece?
        val rookCastlePos: IntOffset?

        when (position) {
            IntOffset('G'.code, 1) -> {
                rookCastle = _pieces.find { it.type == 'R' && it.color == color && it.position == IntOffset('H'.code, 1) }
                rookCastlePos = IntOffset('F'.code, 1)}
            IntOffset('C'.code, 1) -> {
                rookCastle = _pieces.find { it.type == 'R' && it.color == color && it.position == IntOffset('A'.code, 1) }
                rookCastlePos = IntOffset('D'.code, 1)}
            IntOffset('G'.code, 8) -> {
                rookCastle = _pieces.find { it.type == 'R' && it.color == color && it.position == IntOffset('H'.code, 8) }
                rookCastlePos = IntOffset('F'.code, 8)}
            IntOffset('C'.code, 8) -> {
                rookCastle = _pieces.find { it.type == 'R' && it.color == color && it.position == IntOffset('A'.code, 8) }
                rookCastlePos = IntOffset('D'.code, 8)}
            else -> return
        }


        rookCastle?.position = rookCastlePos
        rookCastle?.hasMoved = true
    }

    /*
        Public Methods
     */

    fun getPiece(x:Int, y:Int): Piece? = _pieces.find { it.position.x == x && it.position.y == y }

    fun isAvailableMove(x: Int, y: Int): Boolean = selectedPieceMoves.any { it.x == x && it.y == y }

    /*
        Private Methods
     */

    private fun getCurrentTurn(currentFEN: String): Piece.Color {
        val turnString = currentFEN.split(' ')[1]
        return if (turnString == "w") Piece.Color.White else Piece.Color.Black
    }

    private fun movePiece(piece: Piece, position: IntOffset) {
        val targetPiece = pieces.find { it.position == position }
        val enLook = currentFEN.split(' ')[3]
        val enCheck = (enLook != "-")

        if (targetPiece != null) {
            removePiece(targetPiece)
        }

        if (enCheck && position == convertOffset(enLook)) {
            val targetEn = pieces.find { it.position == enPassantTarget && it.type == 'P' }
            if (targetEn != null) {
                removePiece(targetEn)
            }
        }

        piece.position = position
        piece.hasMoved = true
    }

    private fun removePiece(piece: Piece) {
        _pieces.remove(piece)
    }

    private fun clearSelection() {
        selectedPiece = null
        selectedPieceMoves = emptySet()
    }

    private fun switchPlayerTurn(currentFEN: String) {
        val turnString = currentFEN.split(' ')[1]
        playerTurn =
        if (turnString == "w") {
            Piece.Color.Black
        } else { Piece.Color.White }
    }

    private fun updateFenString(
        pieces: List<Piece>,
        playerTurn: Piece.Color,
        enPassant: Boolean = false,
        enPos: IntOffset = IntOffset(0, 0)
    ) {
        val previousState = currentFEN
        currentFEN = encode(pieces, playerTurn, previousState, enPassant, enPos)
    }


}

@Composable
fun Board.rememberPieceAt(x: Int, y: Int): Piece? = remember(x, y, moveIncrementAction) { getPiece(x = x, y = y) }

@Composable
fun Board.rememberIsAvailableMove(x: Int, y: Int): Boolean = remember(x, y, selectedPieceMoves) { isAvailableMove(x, y) }
