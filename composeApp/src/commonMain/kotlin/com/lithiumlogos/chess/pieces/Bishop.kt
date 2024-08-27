package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset
import chessgame.composeapp.generated.resources.Res
import chessgame.composeapp.generated.resources.bishop_Black
import chessgame.composeapp.generated.resources.bishop_White
import org.jetbrains.compose.resources.DrawableResource

class Bishop (
    override val color: Piece.Color,
    override var position: IntOffset
): Piece {
    override var hasMoved: Boolean = false

    override val type:Char = Type

    override val drawable: DrawableResource =
        if (color.isWhite)
            Res.drawable.bishop_White
        else
            Res.drawable.bishop_Black

    override fun getAvailableMoves(pieces: List<Piece>, fenString: String): Set<IntOffset> {


        return getPieceMoves(pieces, fenString) {
            diagonalMoves()
        }
    }

    companion object {
        const val Type = 'B'
    }
}
