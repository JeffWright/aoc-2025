package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.lib.charsAtPosition
import dev.jtbw.aoc2025.lib.digitsToLong
import dev.jtbw.aoc2025.lib.splitWhitespace
import dev.jtbw.aoc2025.lib.twodeespace.Grid
import dev.jtbw.aoc2025.lib.twodeespace.bounds
import dev.jtbw.aoc2025.lib.twodeespace.get

object Day6 : AoCDay {

  override suspend fun part1(input: String): Any {
    val grid: Grid<String> = input.trim().lines().map { line -> line.splitWhitespace() }
    val bounds = grid.bounds
    return (bounds.left..bounds.right).sumOf { x ->
      val opString = grid[x, bounds.bottom]
      val (op, identity) = getOperation(opString)
      (bounds.top..<bounds.bottom)
        .map { grid[x, it].toLong() }
        .fold(identity) { acc, long -> op(acc, long) }
    }
  }

  override suspend fun part2(input: String): Any {
    val lines = input.trimEnd().lines()
    val ops = lines.last().splitWhitespace().asSequence().map { getOperation(it) }.iterator()
    val numbers = lines.dropLast(1)

    var (op, acc) = ops.next()

    val answers = sequence {
      for (idx in 0..numbers.first().lastIndex) {
        val cephNumber =
          numbers
            .charsAtPosition(idx)
            .filter { it != ' ' }
            .filterNotNull()
            .asIterable()
            .digitsToLong()

        if (cephNumber == 0L) {
          // blank column, next problem
          yield(acc)
          ops.next().let {
            op = it.first
            acc = it.second
          }
        } else {
          acc = op(acc, cephNumber)
        }
      }
      yield(acc)
    }

    return answers.sum()
  }

  private fun getOperation(opString: String): Pair<(Long, Long) -> Long, Long> {
    val op: (Long, Long) -> Long
    val identity: Long

    when (opString) {
      "*" -> {
        op = { a, b -> a * b }
        identity = 1L
      }
      "+" -> {
        op = { a, b -> a + b }
        identity = 0L
      }
      else -> error("")
    }
    return op to identity
  }
}
