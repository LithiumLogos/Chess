package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset
import chessgame.composeapp.generated.resources.Res
import chessgame.composeapp.generated.resources.rook_Black
import chessgame.composeapp.generated.resources.rook_White
import org.jetbrains.compose.resources.DrawableResource

class Rook (
    override val color: Piece.Color,
    override var position: IntOffset
): Piece {
    override var hasMoved: Boolean = false

    override val type:Char = Type

    override val drawable: DrawableResource =
        if (color.isWhite)
            Res.drawable.rook_White
        else
            Res.drawable.rook_Black

    override fun getAvailableMoves(pieces: List<Piece>, fenString: String): Set<IntOffset> {

        return getPieceMoves(pieces, fenString) {
            straightMoves()
        }
    }

    companion object {
        const val Type = 'R'
    }
}
