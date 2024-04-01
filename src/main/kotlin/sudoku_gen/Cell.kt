package sudoku_gen

data class Cell(
    val coord: Coord,
    val value: Int = 0,
) {
    fun isEmpty() = value == 0

    fun isNonEmpty() = !isEmpty()
}
