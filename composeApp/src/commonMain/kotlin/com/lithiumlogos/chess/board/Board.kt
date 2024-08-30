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
import com.lithiumlogos.chess.ai_decision_making.RandomMove
import com.lithiumlogos.chess.pieces.Piece
import com.lithiumlogos.chess.pieces.convertOffset
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import kotlin.math.abs

@Immutable
class Board(val fenString: String, private val playerColor: String = "White") {
    private val _pieces = mutableStateListOf<Piece>()
    val pieces get() = _pieces.toList()
    val setup = fenString.ifBlank { DEFAULT_FEN_SETUP }
    val computerColor = if (playerColor == "White") Piece.Color.Black else Piece.Color.White

    private val moveclip = AudioSystem.getClip()
    private val audioInputStream = AudioSystem.getAudioInputStream(File(MOVE_SOUND))

    init {
        decode(setup).forEach { piece -> _pieces.add(piece) }
        moveclip.open(audioInputStream)
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

    var checkMate by mutableStateOf(false)

    /*
        Network Decision-Making
     */

    private val computerIsWhite = if (computerColor.isWhite) networkMakeMove(this, computerColor) else null

    private fun networkMakeMove(board: Board, computerColor: Piece.Color) {
        if (computerColor != playerTurn) {
            return
        } else {
            val move = RandomMove(board, computerColor).decision()
            val desiredPiece = move.piece
            val desiredMove = move.toSquare

//            Thread.sleep(clip.microsecondLength / 1000)
            println("LiLY: ${move.notation}")
            selectPiece(desiredPiece)
            moveSelectedPiece(desiredMove.x, desiredMove.y)
        }
    }

    /*
        User Events
     */

    private fun playSound(clip: Clip) {
        if (clip.isRunning) {
            clip.stop()
        }

        clip.framePosition = 0
        clip.start()
    }

    private fun isInCheck(pieces: List<Piece> = _pieces) : Boolean {
        val opponentColor = if (playerTurn.isWhite) Piece.Color.Black else Piece.Color.White
        val opponentPiecesThatCanCheck = mutableListOf<Piece>()
        val playerKing = pieces.find { it.type == 'K' && it.color != opponentColor}

        pieces.forEach { piece ->
            if (piece.color == opponentColor) {
                val moves = piece.getAvailableMoves(pieces, currentFEN)
                if (moves.contains(playerKing?.position)) {
                    opponentPiecesThatCanCheck.add(piece)
                }
            }
        }

        return opponentPiecesThatCanCheck.size > 0
    }

    private fun wouldBeCheck(selectedPiece: Piece?, x: Int, y: Int) : Boolean {
        var result = false
        val tempBoard = Board(currentFEN)
        var tempPieces = tempBoard.pieces
        val tempSelection = tempPieces.find { it.position == selectedPiece?.position }
        val tempTarget = tempPieces.find { it.position == IntOffset(x, y) }
        tempSelection?.let { piece ->
            if (tempTarget != null) {
                tempPieces = tempBoard.pieces.minus(tempTarget)
            }
            tempMovePiece(piece = piece, position = IntOffset(x, y), tempBoard)
            result = isInCheck(tempPieces)
        }

        return result
    }

    private fun checkMate(): Boolean {
        if (!isInCheck()) { return false }

        val playerColor = if (playerTurn.isWhite) Piece.Color.White else Piece.Color.Black

        pieces.forEach { piece ->
            if (piece.color == playerColor) {
                val moves = piece.getAvailableMoves(pieces, currentFEN)
                for (move in moves) {
                    if (!wouldBeCheck(piece, move.x, move.y)) {
                        println("${piece.type}: (${BoardXCoordinates.find { it == move.x }?.toChar()}, ${move.y})")
                        return false
                    }
                }
            }
        }

        return true
    }

    private fun tempMovePiece(piece: Piece, position: IntOffset, tempBoard: Board) {
        val targetPiece = tempBoard.pieces.find { it.position == position }

        if (targetPiece != null) {
            tempBoard.pieces.minus(targetPiece)
        }

        piece.position = position
        piece.hasMoved = true
    }

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
        if (checkMate()) {
            println("!!!CHECK MATE!!!")
            checkMate = true
            return
        }


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

            if (wouldBeCheck(selectedPiece, x, y)) {
                println("CHECK")
                return
            }

            // "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
            val halfMoveClock = currentFEN.split(" ")[4].toInt()
            if (halfMoveClock >= 100) {
                println("!!!DRAW!!!")
                return
            }

            moveRookCastle(piece = piece, position = IntOffset(x, y))
            val pieceRemoved = movePiece(piece = piece, position = IntOffset(x, y))
            clearSelection()
            switchPlayerTurn(currentFEN)
            updateFenString(pieces, playerTurn, enPassant, IntOffset(x, enY), pieceRemoved)
            moveIncrementAction++

            playSound(moveclip)
            networkMakeMove(this, computerColor)
        }
    }

    private fun moveRookCastle(piece: Piece, position: IntOffset) {
        if (isInCheck() || piece.type != 'K' || piece.hasMoved) {
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
            else -> {
                return
            }
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

    private fun movePiece(piece: Piece, position: IntOffset) : Boolean {
        var pieceRemoved = false
        val targetPiece = pieces.find { it.position == position }
        val enLook = currentFEN.split(' ')[3]
        val enCheck = (enLook != "-")

        if (targetPiece != null) {
            pieceRemoved = true
            removePiece(targetPiece)
        }

        if (enCheck && position == convertOffset(enLook) && piece.type == 'P') {
            val targetEn = pieces.find { it.position == enPassantTarget && it.type == 'P' }
            if (targetEn != null) {
                pieceRemoved = true
                removePiece(targetEn)
            }
        }

        piece.position = position
        piece.hasMoved = true
        return pieceRemoved
    }

    private fun removePiece(piece: Piece) {
        println("$piece")
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
        enPos: IntOffset = IntOffset(0, 0),
        pieceRemoved: Boolean = false
    ) {
        val previousState = currentFEN
        currentFEN = encode(pieces, playerTurn, previousState, enPassant, enPos, pieceRemoved)
    }


}

@Composable
fun Board.rememberPieceAt(x: Int, y: Int): Piece? = remember(x, y, moveIncrementAction) { getPiece(x = x, y = y) }

@Composable
fun Board.rememberIsAvailableMove(x: Int, y: Int): Boolean = remember(x, y, selectedPieceMoves) { isAvailableMove(x, y) }
