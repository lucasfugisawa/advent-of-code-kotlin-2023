fun main() {

    fun splitInput(input: String): List<String> = input.split(": ", ", ", "; ", " ")

    fun part1(input: List<String>): Int {
        val limits = mapOf("red" to 12, "green" to 13, "blue" to 14)

        fun gameViability(input: String): Pair<Int, Boolean> {
            val split = splitInput(input)
            val gameNumber = split[1].toInt()
            for (index in 2..<split.size step 2) {
                val color = split[index + 1]
                val value = split[index].toInt()
                if (value > (limits[color] ?: Int.MAX_VALUE)) {
                    return gameNumber to false
                }
            }
            return gameNumber to true
        }

        return input.map(::gameViability)
            .filter { (_, isViable) -> isViable }
            .sumOf { (gameNumber, _) -> gameNumber }
    }

    fun part2(input: List<String>): Int {

        fun minimumCubesPower(input: String): Int {
            val maxQuantityGrabbed = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
            val split = splitInput(input)
            for (index in 2..<split.size step 2) {
                val color = split[index + 1]
                val value = split[index].toInt()
                maxQuantityGrabbed.merge(color, value, ::maxOf)
            }
            return maxQuantityGrabbed.values.reduce(Int::times)
        }

        return input.map(::minimumCubesPower).sum()
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
