package sudoku_gen

data class Coord(val row: Int, val col: Int) {
    override fun toString(): String {
        return "$row:$col"
    }
}