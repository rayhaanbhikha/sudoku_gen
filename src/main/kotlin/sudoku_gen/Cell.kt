package com.example.sudoku_gen

class Cell(
    val coord: Coord,
    value: Int = 0,
    val prePopulated: Boolean = false,
    notes: Set<Int> = setOf(),
    var hasMistake: Boolean = false
) {

    val boxRanges = getBoxRanges()

    private var _notes = notes.toMutableSet()

    val notes: Set<Int>
        get() = _notes.toSet()

    var value: Int = value
        private set

    fun setValue(newValue: Int, mistake: Boolean = false) {
        value = newValue
        hasMistake = mistake
        _notes.clear()
    }

    fun addNote(num: Int) {
        value = 0
        if (_notes.contains(num)) {
            _notes.remove(num)
        } else {
            _notes.add(num)
        }
    }

    fun hasNotes(): Boolean = notes.isNotEmpty()

    fun isEmpty() = value == 0

    fun isNonEmpty() = !isEmpty()

    override fun equals(other: Any?): Boolean {
        if (other !is Cell) return false
        return coord == other.coord && value == other.value && other.notes == notes && other.hasMistake == hasMistake
    }

    companion object {
        fun from(coord: Coord, value: Int) = Cell(
            coord = coord,
            value = value,
            prePopulated = true,
        )
    }
}

fun Cell.getBoxRanges(): Pair<IntRange,IntRange> {
    val (row, col) = this.coord
    return when {
        row in 0..2 && col in 0..2 -> Pair(0..2, 0..2)
        row in 0..2 && col in 3..5 -> Pair(0..2, 3..5)
        row in 0..2 && col in 6..8 -> Pair(0..2, 6..8)
        row in 3..5 && col in 0..2 -> Pair(3..5, 0..2)
        row in 3..5 && col in 3..5 -> Pair(3..5, 3..5)
        row in 3..5 && col in 6..8 -> Pair(3..5, 6..8)
        row in 6..8 && col in 0..2 -> Pair(6..8, 0..2)
        row in 6..8 && col in 3..5 -> Pair(6..8, 3..5)
        else -> Pair(6..8, 6..8)
    }
}

class LinkedCellsIterator(private val grid: Grid, currentCell: Cell): Iterator<Cell> {
    private val linkedCells = mutableListOf<Cell>()

    init {
        // loop throw rows
        for (rowIndex in 0..grid.maxRow) {
            val coordToCheck = currentCell.coord.copy(row = rowIndex)
            val otherCell = grid[coordToCheck]
            if (otherCell.coord != currentCell.coord) {
                linkedCells.add(otherCell)
            }
        }

        // loop through columns
        for (colIndex in 0..grid.maxCol) {
            val coordToCheck = currentCell.coord.copy(col = colIndex)
            val otherCell = grid[coordToCheck]
            if (otherCell.coord != currentCell.coord) {
                linkedCells.add(otherCell)
            }
        }

        // check box
        val (rowRange, colRange) = currentCell.boxRanges
        for (boxRowIndex in rowRange) {
            for (boxColIndex in colRange) {
                val coordToCheck = currentCell.coord.copy(row = boxRowIndex, col = boxColIndex)
                val otherCell = grid[coordToCheck]
                if (otherCell.coord != currentCell.coord) {
                    linkedCells.add(otherCell)
                }
            }
        }
    }

    override fun hasNext() = linkedCells.isNotEmpty()

    override fun next() = linkedCells.removeFirst()
}
