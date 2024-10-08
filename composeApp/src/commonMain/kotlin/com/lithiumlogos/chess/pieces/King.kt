package com.lithiumlogos.chess.pieces

import androidx.compose.ui.unit.IntOffset
import chessgame.composeapp.generated.resources.Res
import chessgame.composeapp.generated.resources.king_Black
import chessgame.composeapp.generated.resources.king_White
import org.jetbrains.compose.resources.DrawableResource

class King (
    override val color: Piece.Color,
    override var position: IntOffset
): Piece {
    override var hasMoved: Boolean = false

    override val type:Char = Type

    override val drawable: DrawableResource =
        if (color.isWhite)
            Res.drawable.king_White
        else
            Res.drawable.king_Black

    override fun getAvailableMoves(pieces: List<Piece>, fenString: String): Set<IntOffset> {

        return getPieceMoves(pieces, fenString) {
            straightMoves(maxMovements = 1)
            diagonalMoves(maxMovements = 1)
            castleMoves(fenString)
        }
    }

    companion object {
        const val Type = 'K'
    }
}
