package sudoku_gen

class GridIterator(private val grid: Grid) : Iterator<Cell> {
    private var row = 0
    private var col = 0

    override fun hasNext(): Boolean {
        return row <= grid.maxRow
    }

    override fun next(): Cell {
        val item = grid[Coord(row, col)]
        when (col) {
            grid.maxCol -> {
                col = 0
                row++
            }

            else -> col++
        }

        return item
    }
}


class Grid : Iterable<Cell> {
    private val gridValues: MutableMap<Coord, Cell>
    val maxRow: Int = 8
    val maxCol: Int = 8

    constructor(values: Map<Coord, Cell>) {
        this.gridValues = values.toMutableMap()
    }

    companion object {
        // extension functions added separately
    }

    constructor() {
        gridValues = mutableMapOf()
        for (rowIndex in 0..maxRow) {
            for (colIndex in 0..maxCol) {
                val coord = Coord(rowIndex, colIndex)
                this.gridValues[coord] = defaultValue(coord)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Grid) return false
        return other.gridValues == gridValues
    }

    override fun iterator(): Iterator<Cell> {
        return GridIterator(this)
    }

    operator fun get(coord: Coord) = gridValues[coord] ?: defaultValue(coord)

    operator fun set(coord: Coord, value: Cell) {
        gridValues[coord] = value
    }

    fun clearCell(coord: Coord) {
        gridValues[coord] = defaultValue(coord)
    }

    fun copy(): Grid {
        return Grid(gridValues)
    }

    private fun defaultValue(coord: Coord) = Cell(coord = coord)

    fun isComplete() = this.all { !it.isEmpty() }
}

fun Grid.legal(coord: Coord, value: Int): Boolean {
    for (index in 0..8) {
        val rowCoord = coord.copy(row = index)
        val colCoord = coord.copy(col = index)
        if (rowCoord == colCoord && rowCoord == coord) {
            continue
        }
        if (this[rowCoord].value == value || this[colCoord].value == value) {
            return false
        }
    }

    // Check 3x3 box
    val boxRowStart = coord.row - (coord.row % 3)
    val boxColStart = coord.col - (coord.col % 3)
    for (row in boxRowStart until boxRowStart + 3) {
        for (col in boxColStart until boxColStart + 3) {
            val newCoord = coord.copy(row = row, col = col)
            if (newCoord == coord) continue
            if (this[newCoord].value == value) return false
        }
    }


    return true
}

fun Grid.nextCoord(currentCoord: Coord): Coord? {
//    return when {
//        // reached end of grid.
//        currentCoord.col == this.maxCol && currentCoord.row == this.maxRow -> null
//        // reached end of column but not rows.
//        currentCoord.col == this.maxCol && currentCoord.row < this.maxRow -> Coord(
//            col = 0,
//            row = currentCoord.row + 1
//        )
//        // next coord in column.
//        currentCoord.col < this.maxCol -> currentCoord.copy(
//            col = currentCoord.col + 1,
//        )
//        else -> null
//    }
    // Row is consistently faster.
    return when {
        currentCoord.row < this.maxRow && currentCoord.col < this.maxCol -> currentCoord.copy(
            row = currentCoord.row + 1
        )
        currentCoord.row == this.maxRow && currentCoord.col < this.maxCol -> Coord(
            row = 0,
            col = currentCoord.col + 1
        )
        currentCoord.col == this.maxCol && currentCoord.row < this.maxRow -> currentCoord.copy(
            row = currentCoord.row + 1
        )
        else -> null
    }
}

fun Grid.print() {
    val row = "------+-------+------"
    val emptyValue = "."
    val space = " "
    val divider = " | "
    println()
    for (cell in this) {
        when (cell.value) {
            0 -> print(emptyValue)
            else -> print("${cell.value}")
        }
        when (cell.coord.col) {
            maxCol -> {
                println()
                when (cell.coord.row) {
                    2, 5 -> {
                        println(row)
                    }
                }
            }

            2, 5 -> print(divider)
            else -> print(space)
        }
    }
    println()
}

val defaultSetOfNums = setOf(1,2,3,4,5,6,7,8,9)

fun Grid.fillRandom(): Boolean {
    if (this.isComplete()) return true

    for (cell in this) {
        // if cell is not empty then continue.
        if (!cell.isEmpty()) continue

        // random value is selected which is valid.
        for (possibleValue in defaultSetOfNums.shuffled()) {
            if (!this.legal(cell.coord, possibleValue)) continue
            // set cell value
            this[cell.coord] = Cell(coord = cell.coord, value = possibleValue)
            if (this.fillRandom()) {
                return true
            } else {
                this[cell.coord] = Cell(coord = cell.coord, value = 0)
            }
        }
        // no possible values worked need to backtrack.
        return false
    }
    return false
}

fun Grid.Companion.from(values: List<List<Int>>): Grid {
    val gridValues = mutableMapOf<Coord, Cell>()
    values.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, value ->
            val coord = Coord(rowIndex, colIndex)
            gridValues[coord] = Cell(coord, value)
        }
    }
    return Grid(gridValues)
}

fun Grid.Companion.from(rawString: String): Grid {
    val gridValues = mutableMapOf<Coord, Cell>()
    var rowIndex = 0
    var colIndex = 0

    val chars = rawString.split("").drop(1).dropLast(1)

    for (value in chars) {
        val cellValue = if (value == ".") {
            0
        } else {
            value.toInt()
        }
        val cell = Cell(coord = Coord(rowIndex, colIndex), value = cellValue)
        gridValues[cell.coord] = cell

        if (colIndex == 8) {
            colIndex = 0
            rowIndex++
        } else {
            colIndex++
        }
    }

    return Grid(gridValues)
}

fun Grid.getString(): String {
    return this.joinToString("") { cell ->
        when {
            cell.isEmpty() -> "."
            else -> cell.value.toString()
        }
    }
}