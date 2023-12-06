package day4

import println
import readInput
import kotlin.math.pow

fun main() {

    fun part1(lines: List<String>): Int =
        lines.map { line -> line.toCard() }
            .sumOf { card -> card.points }

    fun part2(lines: List<String>): Int = 30 // TODO: Implement.

    // test if implementation meets criteria from the description:
    val testInput = readInput("day4/puzzleTestData.txt")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    // calculate puzzle data solution:
    val input = readInput("day4/puzzleData.txt")
    part1(input).println()
    part2(input).println()
}

class Card(
    val number: Int,
    val winningNumbers: List<Int>,
    val availableNumbers: List<Int>,
) {
    val points: Int by lazy {
        availableNumbers
            .count { it in winningNumbers }
            .let(::calculatePoints)
    }

    private fun calculatePoints(numberOfWinningNumbers: Int): Int =
        if (numberOfWinningNumbers == 0) 0 else 2.0.pow(numberOfWinningNumbers - 1).toInt()

    override fun toString(): String =
        "Card $number: ${winningNumbers.joinToString(" ")} | ${availableNumbers.joinToString(" ")} -> $points"
}

private val cardRegex = """Card\s+(\d+):\s+(\d+(?:\s+\d+)*)\s*\|\s*(\d+(?:\s+\d+)*)""".toRegex()

fun String.toCard(): Card {
    val matchResult = cardRegex.find(this)
    if (matchResult != null) {
        val (cardNumber, winningNumbers, availableNumbers) = matchResult.destructured
        return Card(
            cardNumber.toInt(),
            winningNumbers.split("\\s+".toRegex()).map { it.toInt() },
            availableNumbers.split("\\s+".toRegex()).map { it.toInt() },
        )
    } else {
        throw IllegalArgumentException("Invalid card line: $this")
    }
}