package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset

fun Piece.getPieceMoves(pieces: List<Piece>, fenString: String, block: PieceMovementBuilder.() -> Unit): Set<IntOffset> {
    val builder = PieceMovementBuilder(piece = this, pieces = pieces, fenString = fenString)
    builder.block()

    return builder.build()
}

class PieceMovementBuilder(private val piece: Piece, private val pieces: List<Piece>, private val fenString: String) {
    private val moves = mutableSetOf<IntOffset>()

    fun castleMoves(
        fenString: String
    ) {
        moves.addAll(
            piece.getCastleMoves(
                pieces = pieces,
                fenString = fenString
            )
        )
    }


    fun straightMoves(
        canCapture: Boolean = true,
        maxMovements: Int = 7
    ) {
        StraightMovement.entries.forEach { movement ->
            straightMoves(
                movement = movement,
                canCapture = canCapture,
                maxMovements = maxMovements
            )
        }
    }

    fun straightMoves(
        movement: StraightMovement,
        canCapture: Boolean = true,
        maxMovements: Int = 7
    ) {
        moves.addAll(
            piece.getStraightMoves(
                pieces = pieces,
                movement = movement,
                canCapture = canCapture,
                maxMovements = maxMovements,

            )
        )
    }
    fun diagonalMoves(
        canCapture: Boolean = true,
        captureOnly: Boolean = false,
        maxMovements: Int = 7
    ) {
        DiagonalMovement.entries.forEach { movement ->
            diagonalMoves(
                movement = movement,
                canCapture = canCapture,
                captureOnly = captureOnly,
                maxMovements = maxMovements
            )
        }
    }

    fun diagonalMoves(
        movement: DiagonalMovement,
        canCapture: Boolean = true,
        captureOnly: Boolean = false,
        maxMovements: Int = 7
    ) {
        moves.addAll(
            piece.getDiagonalMoves(
                pieces = pieces,
                movement = movement,
                canCapture = canCapture,
                captureOnly = captureOnly,
                maxMovements = maxMovements,

            )
        )
    }

    fun getLMoves() {
        moves.addAll(
            piece.getLMoves(
                pieces = pieces
            )
        )
    }

    fun build(): Set<IntOffset> = moves.toSet()

}
