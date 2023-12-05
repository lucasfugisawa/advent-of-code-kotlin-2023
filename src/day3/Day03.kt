package day3

import println
import readInput

fun main() {

    fun part1(engineSchematics: EngineSchematics): Int {
        return engineSchematics.partNumbers
            .filter { it.valid }
            .sumOf { it.number }
    }

    fun part2(engineSchematics: EngineSchematics): Int {
        return engineSchematics.engines
            .filter { it.valid }
            .mapNotNull { it.gearRatio }
            .sum()
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("day3/puzzleTestData.txt")
    val testEngineSchematics = EngineSchematics(testInput)
    check(part1(testEngineSchematics) == 4361)
    check(part2(testEngineSchematics) == 467835)

    // calculate puzzle data solution:
    val input = readInput("day3/puzzleData.txt")
    val engineSchematics = EngineSchematics(input)
    part1(engineSchematics).println()
    part2(engineSchematics).println()
}

typealias LineRange = IntRange
typealias ColumnRange = IntRange

typealias SchematicsRange = Pair<LineRange, ColumnRange>

fun SchematicsRange.collides(other: SchematicsRange): Boolean {
    return this.first.any { it in other.first } && this.second.any { it in other.second }
}

data class PartNumber(
    var number: Int = 0,
    val line: Int = 0,
    val column: Int = 0,
    var valid: Boolean = false,
) {
    var schematicsRange: SchematicsRange = (line..line) to (column..column)
        private set

    fun addDigit(digit: Int) {
        require(digit in 0..9)
        schematicsRange = schematicsRange.first to (schematicsRange.second.first..schematicsRange.second.last + 1)
        number = number * 10 + digit
    }
}

data class Engine(
    val symbol: Char,
    val line: Int = 0,
    val column: Int = 0,
    var valid: Boolean = false,
) {
    val schematicsRange: SchematicsRange = (line - 1..line + 1) to (column - 1..column + 1)
    var gearRatio: Int? = null
}

class EngineSchematics(
    private val input: List<String>,
) {

    val engines: MutableSet<Engine> = mutableSetOf<Engine>()
    val partNumbers: MutableSet<PartNumber> = mutableSetOf<PartNumber>()

    init {
        var currentPartNumber: PartNumber? = null
        input.forEachIndexed { lineIndex, lineString ->
            lineString.forEachIndexed { charIndex, char ->
                when (char) {
                    in '0'..'9' -> {
                        if (currentPartNumber == null) {
                            currentPartNumber = PartNumber(char.digitToInt(), lineIndex, charIndex)
                        } else {
                            currentPartNumber!!.addDigit(char.digitToInt())
                        }
                    }

                    '.' -> {
                        if (currentPartNumber != null) {
                            partNumbers.add(currentPartNumber!!)
                            currentPartNumber = null
                        }
                    }

                    else -> {
                        if (currentPartNumber != null) {
                            partNumbers.add(currentPartNumber!!)
                            currentPartNumber = null
                        }
                        engines.add(Engine(char, lineIndex, charIndex))
                    }
                }
            }
        }
        updateValidity()
    }

    private fun updateValidity() {
        partNumbers.filter { !it.valid }
            .forEach { partNumber ->
                partNumber.valid = engines.any { it.schematicsRange.collides(partNumber.schematicsRange) }
            }
        engines
            .forEach { engine ->
                val validPartNumbers = partNumbers.filter { it.schematicsRange.collides(engine.schematicsRange) }
                engine.gearRatio = validPartNumbers.map { it.number }.reduceOrNull(Int::times)
                engine.valid = validPartNumbers.count() == 2
            }
    }
}

