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

@Composable
fun rememberBoard(fenString: String = DEFAULT_FEN_SETUP): Board =
    remember { Board(fenString) }

@Immutable
class Board(val fenString: String) {
    private val _pieces = mutableStateListOf<Piece>()
    val pieces get() = _pieces.toList()
    val setup = fenString.ifBlank { DEFAULT_FEN_SETUP }

    init {
        decode(setup).forEach { piece -> _pieces.add(piece) }

        var playerTurn = getCurrentTurn(setup)
    }

    var selectedPiece by mutableStateOf<Piece?>(null)
        private set

    var selectedPieceMoves by mutableStateOf(emptySet<IntOffset>())
        private set

    var moveIncrementAction by mutableIntStateOf(0)
        private set

    var playerTurn by mutableStateOf(getCurrentTurn(setup))


    var currentFEN by mutableStateOf(encode(pieces, playerTurn))

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
            selectedPieceMoves = piece.getAvailableMoves(pieces = pieces)

        }
    }

    fun moveSelectedPiece(x:Int , y:Int ) {
        selectedPiece?.let { piece ->
            if (!isAvailableMove(x = x, y = y)) {
                return
            }

            if (piece.color !== playerTurn) {
                return
            }

            movePiece(piece = piece, position = IntOffset(x, y))
            clearSelection()
            switchPlayerTurn(currentFEN)
            updateFenString(pieces, playerTurn)
            moveIncrementAction++

        }
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

        if (targetPiece != null) {
            removePiece(targetPiece)
        }

        piece.position = position
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

    private fun updateFenString(pieces: List<Piece>, playerTurn: Piece.Color ) {
        currentFEN = encode(pieces, playerTurn)
    }


}

@Composable
fun Board.rememberPieceAt(x: Int, y: Int): Piece? = remember(x, y, moveIncrementAction) { getPiece(x = x, y = y) }

@Composable
fun Board.rememberIsAvailableMove(x: Int, y: Int): Boolean = remember(x, y, selectedPieceMoves) { isAvailableMove(x, y) }
