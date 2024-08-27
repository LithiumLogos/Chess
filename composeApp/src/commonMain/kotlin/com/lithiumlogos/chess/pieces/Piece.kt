package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset
import com.lithiumlogos.chess.board.BoardXCoordinates
import com.lithiumlogos.chess.board.BoardYCoordinates
import org.jetbrains.compose.resources.DrawableResource

interface Piece {
    val color: Color

    enum class Color {
        White,
        Black;

        val isWhite: Boolean
            get() = this == White

        val isBlack: Boolean
            get() = this == Black
    }

    val drawable: DrawableResource
    var position: IntOffset
    val type: Char
    var hasMoved: Boolean

    fun getAvailableMoves(pieces: List<Piece>, fenString: String): Set<IntOffset>
}

fun Piece.getEnPassantMoves(
    movement: DiagonalMovement,
    fenString: String
) : Set<IntOffset> {
    val moves = mutableSetOf<IntOffset>()
    // "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq e4 0 1"

    val availableEn = fenString.split(' ')[3].uppercase()
    val enTarget: IntOffset

    if (availableEn == "-") {
        return moves
    } else {
        enTarget = convertOffset(availableEn)
    }

    val enPossibleTarget =
        when (movement) {
            DiagonalMovement.UpLeft -> IntOffset(x = position.x - 1, y = position.y + 1)
            DiagonalMovement.UpRight -> IntOffset(x = position.x + 1, y = position.y + 1)
            DiagonalMovement.DownRight -> IntOffset(x = position.x + 1, y = position.y - 1)
            DiagonalMovement.DownLeft -> IntOffset(x = position.x - 1, y = position.y - 1)
        }

    if (enTarget == enPossibleTarget) {
        moves.add(enPossibleTarget)
    }

    return moves
}

fun Piece.getCastleMoves(
    pieces: List<Piece>,
    fenString: String
) : Set<IntOffset> {
    val moves = mutableSetOf<IntOffset>()

    // "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"

    val availableCastles = fenString.split(' ')[2]
    val offsets = mutableListOf<IntOffset>()

    if (availableCastles == "-") {
        return moves
    }

    if (color == Piece.Color.White) {
        if (availableCastles.contains('K')) {
            val targetPosition = this.position + IntOffset(2, 0)
            val targetRookPos = this.position + IntOffset(1, 0)

            if (castleCheck(pieces, targetPosition, targetRookPos)) {
                moves.add(targetPosition)
            }
        }

        if (availableCastles.contains('Q')) {
            val targetPosition = this.position + IntOffset(-2, 0)
            val targetRookPos = this.position + IntOffset(-1, 0)

            if (castleCheck(pieces, targetPosition, targetRookPos)) {
                moves.add(targetPosition)
            }
        }
    }

    if (color == Piece.Color.Black) {
        if (availableCastles.contains('k')) {
            val targetPosition = this.position + IntOffset(2, 0)
            val targetRookPos = this.position + IntOffset(1, 0)

            if (castleCheck(pieces, targetPosition, targetRookPos)) {
                moves.add(targetPosition)
            }
        }
    }

    if (color == Piece.Color.Black) {
        if (availableCastles.contains('q')) {
            val targetPosition = this.position + IntOffset(-2, 0)
            val targetRookPos = this.position + IntOffset(-1, 0)

            if (castleCheck(pieces, targetPosition, targetRookPos)) {
                moves.add(targetPosition)
            }
        }
    }

    return moves
}

private fun castleCheck(pieces: List<Piece>, targetPosition: IntOffset, targetRookPos: IntOffset): Boolean {
    val targetOne = pieces.find { it.position == targetPosition }
    val targetTwo = pieces.find { it.position == targetRookPos }

    return targetOne == null && targetTwo == null
}

enum class StraightMovement {
    Up,
    Down,
    Left,
    Right
}

fun Piece.getStraightMoves(
    pieces: List<Piece>,
    movement: StraightMovement,
    canCapture: Boolean = true,
    maxMovements: Int = 7
) : Set<IntOffset> {
    return getMoves(
        pieces = pieces,
        getPosition = {
            when (movement) {
                StraightMovement.Up -> IntOffset(x = position.x, y = position.y + it)
                StraightMovement.Down -> IntOffset(x = position.x, y = position.y - it)
                StraightMovement.Left -> IntOffset(x = position.x - it, y = position.y)
                StraightMovement.Right -> IntOffset(x = position.x + it, y = position.y)
            }
        },
        canCapture = canCapture,
        maxMovements = maxMovements
    )
}

enum class DiagonalMovement {
    UpLeft,
    UpRight,
    DownRight,
    DownLeft

}

fun Piece.getDiagonalMoves(
    pieces: List<Piece>,
    movement: DiagonalMovement,
    canCapture: Boolean = true,
    captureOnly: Boolean = false,
    maxMovements: Int = 7
) : Set<IntOffset> {
    return getMoves(
        pieces = pieces,
        getPosition = {
            when (movement) {
                DiagonalMovement.UpLeft -> IntOffset(x = position.x - it, y = position.y + it)
                DiagonalMovement.UpRight -> IntOffset(x = position.x + it, y = position.y + it)
                DiagonalMovement.DownRight -> IntOffset(x = position.x + it, y = position.y - it)
                DiagonalMovement.DownLeft -> IntOffset(x = position.x - it, y = position.y - it)
            }
        },
        canCapture = canCapture,
        captureOnly = captureOnly,
        maxMovements = maxMovements
    )
}

fun Piece.getMoves(
    pieces: List<Piece>,
    getPosition: (Int) -> IntOffset,
    canCapture: Boolean = true,
    captureOnly: Boolean = false,
    maxMovements: Int = 7
) : Set<IntOffset> {
    val moves = mutableSetOf<IntOffset>()

    for (i in 1..maxMovements) {
        val targetPosition = getPosition(i)
        val targetPiece = pieces.find { it.position == targetPosition }

        if (targetPosition.x !in BoardXCoordinates || targetPosition.y !in BoardYCoordinates) {
            break
        }

        if (targetPiece != null) {
            if ((targetPiece.color !== this.color) && canCapture) {
                moves.add(targetPosition)
            }
            break
        } else if (captureOnly) {
            break
        } else { moves.add(targetPosition) }
    }

    return moves
}

fun Piece.getLMoves(pieces: List<Piece>) : Set<IntOffset> {
    val moves = mutableSetOf<IntOffset>()

    val offsets = listOf(
        IntOffset(-1, -2),
        IntOffset(-1, 2),
        IntOffset(1, -2),
        IntOffset(1, 2),
        IntOffset(-2, -1),
        IntOffset(-2, 1),
        IntOffset(2, -1),
        IntOffset(2, 1)
    )

    for (offset in offsets) {
        val targetPosition = position + offset

        if (targetPosition.x !in BoardXCoordinates || targetPosition.y !in BoardYCoordinates) {
            continue
        }

        val targetPiece = pieces.find { it.position == targetPosition }
        if ((targetPiece == null) || (targetPiece.color !== this.color)) {
            moves.add(targetPosition)
        }
    }

    return moves
}

fun convertOffset(position: String) : IntOffset {
    val x = position.uppercase()[0].code
    val y = position[1].digitToInt()
    return IntOffset(x, y)
}
