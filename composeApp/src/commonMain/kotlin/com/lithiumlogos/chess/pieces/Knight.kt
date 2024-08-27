package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset
import chessgame.composeapp.generated.resources.Res
import chessgame.composeapp.generated.resources.knight_Black
import chessgame.composeapp.generated.resources.knight_White
import org.jetbrains.compose.resources.DrawableResource

class Knight (
    override val color: Piece.Color,
    override var position: IntOffset
): Piece {
    override var hasMoved: Boolean = false

    override val type:Char = Type

    override val drawable: DrawableResource =
        if (color.isWhite)
            Res.drawable.knight_White
        else
            Res.drawable.knight_Black

    override fun getAvailableMoves(pieces: List<Piece>, fenString: String): Set<IntOffset> {

        return getPieceMoves(pieces, fenString) {
            getLMoves()
        }
    }

    companion object {
        const val Type = 'N'
    }
}
