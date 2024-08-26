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
fun rememberBoard(): Board = remember { Board() }

@Immutable
class Board {
    private val _pieces = mutableStateListOf<Piece>()
    val pieces get() = _pieces.toList()

    init {
        val defaultSetup = decode(DEFAULT_FEN_SETUP)
        defaultSetup.forEach { piece -> _pieces.add(piece) }
    }

    var selectedPiece by mutableStateOf<Piece?>(null)
        private set

    var selectedPieceMoves by mutableStateOf(emptySet<IntOffset>())
        private set

    var moveIncrementAction by mutableIntStateOf(0)
        private set

    var playerTurn by mutableStateOf(Piece.Color.White)

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
            switchPlayerTurn()
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

    private fun switchPlayerTurn() {
        playerTurn =
        if (playerTurn.isWhite) {
            Piece.Color.Black
        } else { Piece.Color.White }
    }


}

@Composable
fun Board.rememberPieceAt(x: Int, y: Int): Piece? = remember(x, y, moveIncrementAction) { getPiece(x = x, y = y) }

@Composable
fun Board.rememberIsAvailableMove(x: Int, y: Int): Boolean = remember(x, y, selectedPieceMoves) { isAvailableMove(x, y) }
