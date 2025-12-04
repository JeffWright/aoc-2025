package dev.jtbw.aoc2025

object Day3 {
  val sample =
    """
    987654321111111
    811111111111119
    234234234234278
    818181911112111
    """
      .trimIndent()

  fun run() {
    sample(sample, "357", ::part1)
    day(3, 1, ::part1)

    sample(sample, "3121910778619", ::part2)
    day(3, 2, ::part2)
  }

  suspend fun part1(input: String): Any {
    return input.lines().sumOf { line -> joltage(line, 2) }
  }

  suspend fun part2(input: String): Any {
    return input.lines().sumOf { line -> joltage(line, 12) }
  }

  fun joltage(line: String, numDigits: Int): Long {
    val indexed = line.mapIndexed { idx, ch -> IndexedValue(idx = idx, value = ch.digitToInt()) }
    var start = 0
    return (numDigits downTo 1)
      .asSequence()
      .map { num ->
        // Take the biggest digit that's after the ones we've already chosen but leaves enough space
        // at the end
        val pick = indexed.slice(start..indexed.size - num).maxBy { it.value }
        start = pick.idx + 1
        pick.value
      }
      // Convert list of digits to Long
      .fold(0L) { acc, i -> (acc * 10) + i }
  }
}
