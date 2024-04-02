package sudoku_gen

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HelpersKtTest {

    @Test
    fun pokeHoles() {
    }

    @ParameterizedTest
    @MethodSource("numberOfSolutionsInput")
    fun `should return correct number of solutions`(data: NumberOfSolutionsTestData) {
        val grid = Grid.from(data.input)
        val numOfSolutions = numberOfSolutions(grid, 0, coord = Coord(0, 0))
        assertEquals(data.expected, numOfSolutions)
    }

    private fun numberOfSolutionsInput() = Stream.of(
        // 3 solutions
        NumberOfSolutionsTestData("..3.....6...98..2.9426..7..45...6............1.9.5.47.....25.4.6...785.........", 2),
        // 2
        NumberOfSolutionsTestData(
            ".39...12....9.7...8..4.1..6.42...79...........91...54.5..1.9..3...8.5....14...87",
            2
        ),
        // 0 - row
        NumberOfSolutionsTestData(
            "9..1....4.14.3.8....3....9....7.8..18....3..........3..21....7...9.4.5..5...16..3",
            0
        ),
        // 0 - col
        NumberOfSolutionsTestData(
            "....41....6.....2...2......32.6.........5..417.......2......23..48......5.1..2...",
            0
        ),
        // 4
        NumberOfSolutionsTestData(
            "....9....6..4.7..8.4.812.3.7.......5..4...9..5..371..4.5..6..4.2.17.85.9.........",
            2
        ),
        // 10
        NumberOfSolutionsTestData(
            "59.....486.8...3.7...2.1.......4.....753.698.....9.......8.3...2.6...7.934.....65",
            2
        ),
        // 125
        NumberOfSolutionsTestData(
            "...3165..8..5..1...1.89724.9.1.85.2....9.1....4.263..1.5.....1.1..4.9..2..61.8...",
            2
        ),
        // 1
        NumberOfSolutionsTestData(
            ".6.589.....3.......4.6.3..8......1...978...4.....4.3.5........2.1..7.4636.....9.7",
            1
        ),
        NumberOfSolutionsTestData(
            ".7.9....656.3....9...4...2.2.....8.4.9...6..1.1...2.9.9.6..3....23....7.7..1.....",
            1
        ),
        NumberOfSolutionsTestData(
            "....5.47.2.74....1...1...6....928.3...4.....68....59......8......6.97..5.3.....48",
            1
        ),
        NumberOfSolutionsTestData(
            "..7.26....9....2...13.....81.47.9.3..3....7.47.....61....87.....4.2..5.....6...9.",
            1
        ),
    )

    data class NumberOfSolutionsTestData(
        val input: String,
        val expected: Int,
    )
}