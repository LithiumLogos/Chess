package com.lithiumlogos.chess.ai_decision_making

import androidx.compose.ui.unit.IntOffset
import com.lithiumlogos.chess.board.Board
import com.lithiumlogos.chess.pieces.Piece

class RandomMove(val board: Board, val color: Piece.Color) {
    private val currentBoard = board
    private val computerColor = color

    fun decision(): ChessMove {
        val myPieces = currentBoard.pieces.filter { it.color == computerColor }
        val allMoves = mutableListOf<ChessMove>()

        myPieces.forEach { piece ->
            val moves = piece.getAvailableMoves(currentBoard.pieces, currentBoard.currentFEN)
            moves.forEach { move ->
                if (!wouldBeCheck(piece, move.x, move.y)) {
                    allMoves.add(ChessMove(piece, move))
                }
            }
        }

        return allMoves.random()
    }

    // Duplicate because concern that board specific info will flow between

    private fun isInCheck(pieces: List<Piece>) : Boolean {
        val opponentColor = if (computerColor.isWhite) Piece.Color.Black else Piece.Color.White
        val opponentPiecesThatCanCheck = mutableListOf<Piece>()
        val playerKing = pieces.find { it.type == 'K' && it.color != opponentColor}

        pieces.forEach { piece ->
            if (piece.color == opponentColor) {
                val moves = piece.getAvailableMoves(pieces, currentBoard.currentFEN)
                if (moves.contains(playerKing?.position)) {
                    opponentPiecesThatCanCheck.add(piece)
                }
            }
        }

        return opponentPiecesThatCanCheck.size > 0
    }

    private fun wouldBeCheck(selectedPiece: Piece?, x: Int, y: Int) : Boolean {
        var result = false
        val tempBoard = Board(currentBoard.currentFEN)
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

    private fun tempMovePiece(piece: Piece, position: IntOffset, tempBoard: Board) {
        val targetPiece = tempBoard.pieces.find { it.position == position }

        if (targetPiece != null) {
            tempBoard.pieces.minus(targetPiece)
        }

        piece.position = position
        piece.hasMoved = true
    }
}


