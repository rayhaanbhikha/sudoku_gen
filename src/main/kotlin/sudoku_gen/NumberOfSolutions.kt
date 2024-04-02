package sudoku_gen

fun numberOfSolutions(grid: Grid, count: Int = 0, coord: Coord = Coord(0, 0)): Int {
    var countVar = count
    val nextCoord = grid.nextCoord(coord) ?: return (1 + countVar)

    if (grid[coord].isNonEmpty()) return numberOfSolutions(grid, countVar, nextCoord)

    for (value in 1..9) {
        if (!grid.legal(coord, value)) continue

        val newCell = Cell(coord = coord, value)
        grid[newCell.coord] = newCell

        countVar = numberOfSolutions(grid, countVar, nextCoord)
        if (countVar > 1) return countVar
    }
    grid[coord] = Cell(coord = coord, value = 0)

    return countVar
}