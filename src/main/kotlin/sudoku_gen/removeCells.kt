package sudoku_gen

// randomly find elements and poke a hole.
// then try and solve the puzzle
// when solving the puzzle there can only be one solution.
// (consider each 'hole' a possible starting point).
// if no solution or more than 1 solution is found, consider it a failure.
// put hole back.
fun removeCells(grid: Grid, numOfCellsToRemove: Int): Pair<Grid, List<Cell>> {
    // TODO: Depending on number of holes requested (i.e. holes > 55)
    // we should start a few coroutines together.
    val gridWithEmptyCells = grid.copy()
    val cellsRemoved = mutableListOf<Cell>()

    while(cellsRemoved.size < numOfCellsToRemove) {
        val rowIndex = (0..gridWithEmptyCells.maxRow).random()
        val colIndex = (0..gridWithEmptyCells.maxCol).random()
        val coord = Coord(rowIndex, colIndex)
        val cell = gridWithEmptyCells[coord]

        if (cell.isEmpty()) continue // already an empty cell.

        gridWithEmptyCells.clearCell(coord)

        val s = gridWithEmptyCells.copy()

        val numOfSolutions = numberOfSolutions(s)
        if (numOfSolutions == 1) {
            cellsRemoved.add(cell)
        } else {
            gridWithEmptyCells[coord] = cell
        }
    }

    return Pair(gridWithEmptyCells, cellsRemoved)
}