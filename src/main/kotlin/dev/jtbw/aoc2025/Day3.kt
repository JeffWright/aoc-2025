package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.lib.IndexedValue

object Day3 : AoCDay {
  override suspend fun part1(input: String): Any {
    return input.lines().sumOf { line -> joltage(line, 2) }
  }

  override suspend fun part2(input: String): Any {
    return input.lines().sumOf { line -> joltage(line, 12) }
  }

  fun joltage(line: String, numDigits: Int): Long {
    val indexed = line.mapIndexed { idx, ch -> IndexedValue(idx = idx, value = ch.digitToInt()) }
    var start = 0
    return (numDigits downTo 1)
      .asSequence()
      .map { num ->
        // Take the biggest digit that's after the ones we've already chosen but leaves enough
        // space
        // at the end
        val pick = indexed.slice(start..indexed.size - num).maxBy { it.value }
        start = pick.idx + 1
        pick.value
      }
      // Convert list of digits to Long
      .fold(0L) { acc, i -> (acc * 10) + i }
  }
}
