package sudoku_gen

fun main() {
    val exampleGrids = listOf(
        // http://sudopedia.enjoysudoku.com/Invalid_Test_Cases.html#Not_Unique_.E2.80.94_2_Solutions
        // 3 solutions
        "..3.....6...98..2.9426..7..45...6............1.9.5.47.....25.4.6...785.........",
        // 2
        ".39...12....9.7...8..4.1..6.42...79...........91...54.5..1.9..3...8.5....14...87",
        // 0 - row
        "9..1....4.14.3.8....3....9....7.8..18....3..........3..21....7...9.4.5..5...16..3",
        // 0 - col
        "....41....6.....2...2......32.6.........5..417.......2......23..48......5.1..2...",
        // 4
        "....9....6..4.7..8.4.812.3.7.......5..4...9..5..371..4.5..6..4.2.17.85.9.........",
        // 10
        "59.....486.8...3.7...2.1.......4.....753.698.....9.......8.3...2.6...7.934.....65",
        // 125
        "...3165..8..5..1...1.89724.9.1.85.2....9.1....4.263..1.5.....1.1..4.9..2..61.8..."
    )

    for (exampleGrid in exampleGrids) {
        println("### $exampleGrid")
        val grid = stringToGridArray(exampleGrid)
        val sol1 = solve(0, 0, grid, 0)

        println(grid)

        println("1: $sol1")

        val sudokuGrid = Grid.from(exampleGrid)

        val sol3 = numberOfSolutions(
            grid = sudokuGrid,
            count = 0,
            coord = Coord(0, 0)
        )

        println("3: $sol3")

    }
}

fun stringToGridArray(rawString: String): MutableList<MutableList<Int>> {
    var rowIndex = 0
    var colIndex = 0
    var grid = mutableListOf<MutableList<Int>>()

    val chars = rawString.split("").drop(1).dropLast(1)

    for (value in chars) {
        if (grid.size != (rowIndex + 1)) {
            grid.add(mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0))
        }

        if (value != ".") {
            grid[rowIndex][colIndex] = value.toInt()
        }

        if (colIndex == 8) {
            colIndex = 0
            rowIndex++
        } else {
            colIndex++
        }
    }

    return grid
}

fun solve(i: Int, j: Int, cells: MutableList<MutableList<Int>>, count: Int): Int {
    var countVar = count
    var iVar = i
    var jVar = j

    if (iVar == 9) {
        iVar = 0
        if (++jVar == 9)
            return (1 + countVar)
    }

    // iVar is the row.
    // jVar is the col.

    if (cells[iVar][jVar] != 0) return solve(iVar + 1, jVar, cells, countVar)

    // Search for 2 solutions instead of 1. Break if 2 solutions are found
    for (value in 1..9) {
        if (legal(iVar, jVar, value, cells)) {
            cells[iVar][jVar] = value
            // Add additional solutions
            countVar = solve(iVar + 1, jVar, cells, countVar)
            if (countVar >= 2) return countVar
        }
    }
    cells[iVar][jVar] = 0 // Reset on backtrack
    return countVar
}

fun legal(i: Int, j: Int, value: Int, cells: List<List<Int>>): Boolean {
    // Check row and column
    for (index in 0 until 9) {
        if (cells[i][index] == value || cells[index][j] == value)
            return false
    }

    // Check 3x3 box
    val boxRowStart = i - i % 3
    val boxColStart = j - j % 3
    for (row in boxRowStart until boxRowStart + 3) {
        for (col in boxColStart until boxColStart + 3) {
            if (cells[row][col] == value)
                return false
        }
    }

    return true
}
