package day4

import println
import readInput
import kotlin.math.pow

fun main() {

    fun part1(deck: Deck): Int = deck.points

    fun part2(deck: Deck): Int = deck.processedTotal

    // test if implementation meets criteria from the description:
    val testInput = readInput("day4/puzzleTestData.txt")
    val testDeck = Deck(testInput.map(String::toCard))
    check(part1(testDeck) == 13)
    check(part2(testDeck) == 30)

    // calculate puzzle data solution:
    val input = readInput("day4/puzzleData.txt")
    val deck = Deck(input.map(String::toCard))
    part1(deck).println()
    part2(deck).println()
}

class Card(
    private val number: Int,
    val winningNumbers: List<Int>,
    val availableNumbers: List<Int>,
) {
    val points: Int by lazy { availableNumbers.count { it in winningNumbers }.let(::calculatePoints) }

    private fun calculatePoints(matchesCount: Int): Int =
        if (matchesCount == 0) 0 else 2.0.pow(matchesCount - 1).toInt()

    override fun toString(): String {
        fun leftPadWithSpace(number: Int) = number.toString().padStart(2, ' ')
        return "Card $number: " +
                "${winningNumbers.joinToString(" ", transform = ::leftPadWithSpace)} | " +
                "${availableNumbers.joinToString(" ", transform = ::leftPadWithSpace)} -> $points"
    }
}

class Deck(
    private val cards: List<Card>,
) {
    val points: Int by lazy { cards.sumOf(Card::points) }
    val processedTotal: Int by lazy { calculateTotalScratchcards() }

    private fun calculateTotalScratchcards(): Int {
        val counter = IntArray(cards.size) { 1 }

        cards.forEachIndexed { i, card ->
            val score = card.availableNumbers.count { it in card.winningNumbers }
            for (j in (i + 1)..minOf(i + score, cards.size - 1)) {
                counter[j] += counter[i]
            }
        }

        return counter.sum()
    }

    override fun toString(): String = cards.joinToString("\n")
}

private val cardRegex = """Card\s+(\d+):\s+(\d+(?:\s+\d+)*)\s*\|\s*(\d+(?:\s+\d+)*)""".toRegex()

fun String.toCard(): Card =
    cardRegex.find(this)
        ?.destructured
        ?.let { (cardNumber, winningNumbers, availableNumbers) ->
            Card(
                cardNumber.toInt(),
                winningNumbers.split("\\s+".toRegex()).map(String::toInt),
                availableNumbers.split("\\s+".toRegex()).map(String::toInt)
            )
        }
        ?: throw IllegalArgumentException("Invalid card line: $this")
