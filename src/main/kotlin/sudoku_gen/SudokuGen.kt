package sudoku_gen

import kotlin.time.measureTimedValue

fun main() {
    var holedGrid: Grid

    val timeTaken = measureTimedValue {
        val grid = Grid()
        grid.fillRandom()

        val (_holedGrid, holes) = pokeHoles(grid, 55)
        holedGrid = _holedGrid

        grid
    }

    println(timeTaken.duration)

    holedGrid.print()
}


// randomly find elements and poke a hole.
// then try and solve the puzzle
// when solving the puzzle there can only be one solution.
// (consider each 'hole' a possible starting point).
// if no solution or more than 1 solution is found, consider it a failure.
// put hole back.
fun pokeHoles(grid: Grid, holes: Int): Pair<Grid, List<Cell>> {
    // TODO: Depending on number of holes requested (i.e. holes > 55)
    // we should start a few coroutines together.
    val gridWithEmptyCells = grid.copy()
    val cellsRemoved = mutableListOf<Cell>()

    while(cellsRemoved.size < holes) {
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

// TODO: write tests for this function.
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