package com.lithiumlogos.chess.board

// A to H
val BoardXCoordinates = List(8) {
    'A'.code + it
}

// 8 to 1
val BoardYCoordinates = List(8) {
    8 - it
}

// Default Setup in Forsyth-Edwards Notation (FEN)
const val DEFAULT_FEN_SETUP = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"


// Sounds
const val MOVE_SOUND = "src/commonMain/resources/sounds/move_sound.wav"