package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset
import chessgame.composeapp.generated.resources.Res
import chessgame.composeapp.generated.resources.pawn_Black
import chessgame.composeapp.generated.resources.pawn_White
import org.jetbrains.compose.resources.DrawableResource

class Pawn (
    override val color: Piece.Color,
    override var position: IntOffset
): Piece {
    override val type:Char = Type

    override val drawable: DrawableResource =
        if (color.isWhite)
            Res.drawable.pawn_White
        else
            Res.drawable.pawn_Black

    override fun getAvailableMoves(pieces: List<Piece>): Set<IntOffset> {
        val isFirstMove = position.y == 2 && color.isWhite || position.y == 7 && color.isBlack

        return getPieceMoves(pieces) {
            straightMoves(
                movement = if (color.isWhite) StraightMovement.Up else StraightMovement.Down,
                canCapture = false,
                maxMovements = if (isFirstMove) 2 else 1
            )

            diagonalMoves(
                movement = if (color.isWhite) DiagonalMovement.UpRight else DiagonalMovement.DownRight,
                captureOnly = true,
                maxMovements = 1
            )

            diagonalMoves(
                movement = if (color.isWhite) DiagonalMovement.UpLeft else DiagonalMovement.DownLeft,
                captureOnly = true,
                maxMovements = 1
            )
        }
    }

    companion object {
        const val Type = 'P'
    }
}
