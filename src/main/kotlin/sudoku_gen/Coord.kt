package com.example.sudoku_gen

data class Coord(val row: Int, val col: Int) {
//    private val quadrant = when {
//        row in 0..2 && col in 0..2 -> 1
//        row in 0..2 && col in 3..5 -> 2
//        row in 0..2 && col in 6..8 -> 3
//        row in 3..5 && col in 0..2 -> 4
//        row in 3..5 && col in 3..5 -> 5
//        row in 3..5 && col in 6..8 -> 6
//        row in 6..8 && col in 0..2 -> 7
//        row in 6..8 && col in 3..5 -> 8
//        else -> 9
//    }

    override fun toString(): String {
        return "$row:$col"
    }

    private fun sameRow(other: Coord) = row == other.row
    private fun sameCol(other: Coord) = col == other.col
//    private fun sameSubGrid(other: Coord) = other.quadrant == quadrant
//    fun isLinked(other: Coord) = sameRow(other) || sameCol(other) || sameSubGrid(other)

}