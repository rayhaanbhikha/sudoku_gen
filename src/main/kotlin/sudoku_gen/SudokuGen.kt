package sudoku_gen

import kotlin.time.measureTimedValue

fun main() {
    var holedGrid: Grid

    val timeTaken = measureTimedValue {
        val grid = Grid()
        grid.fillRandom()

        val (_holedGrid, holes) = removeCells(grid, 55)
        holedGrid = _holedGrid

        grid
    }

    println(timeTaken.duration)

    holedGrid.print()

    println(holedGrid.getString())
}