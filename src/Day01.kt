fun main() {

    fun part1(input: List<String>): Int {

        fun calibrationValue(word: String): Int {
            val first = word.first { it.isDigit() }.digitToInt()
            val last = word.last { it.isDigit() }.digitToInt()
            return (first * 10 + last)
        }

        return input.sumOf(::calibrationValue)
    }

    fun part2(input: List<String>): Int {

        infix fun String.firstSubstringAmong(words: Set<String>): String? {
            var firstSubstringIndex = Int.MAX_VALUE
            var firstSubstring: String? = null
            for (word in words) {
                val index = this.indexOf(word)
                if (index != -1 && index < firstSubstringIndex) {
                    firstSubstringIndex = index
                    firstSubstring = word
                }
            }
            return firstSubstring
        }

        fun calibrationValue(word: String): Int {
            val digitsMap = mapOf(
                "0" to 0, "1" to 1, "2" to 2, "3" to 3, "4" to 4, "5" to 5, "6" to 6, "7" to 7, "8" to 8, "9" to 9,
                "zero" to 0, "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
            )
            val reversedDigitsMap = digitsMap.map { (key, value) -> key.reversed() to value }.toMap()
            val reversedWord = word.reversed()
            val first = digitsMap[word firstSubstringAmong digitsMap.keys] ?: 0
            val last = reversedDigitsMap[reversedWord firstSubstringAmong reversedDigitsMap.keys] ?: 0
            return (first * 10 + last)
        }
        return input.sumOf(::calibrationValue)
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
