package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.lib.digitsToLong
import dev.jtbw.aoc2025.lib.splitWhitespace
import dev.jtbw.aoc2025.lib.twodeespace.Grid
import dev.jtbw.aoc2025.lib.twodeespace.bounds
import dev.jtbw.aoc2025.lib.twodeespace.get
import kotlin.collections.forEach

object Day6 : AoCDay {

  override suspend fun part1(input: String): Any {
    val grid: Grid<String> = input.lines().map { line -> line.splitWhitespace() }
    val bounds = grid.bounds
    return (bounds.left..bounds.right).sumOf { x ->
      val opString= grid[x, bounds.bottom]
      val (op, identity) = getOperation(opString)
      (bounds.top..<bounds.bottom)
        .map { grid[x, it].toLong() }
        .fold(identity) {acc, long -> op(acc, long)}
    }
  }

  override suspend fun part2(input: String): Any {
    val grid: Grid<String> = input.lines().map { line -> line.splitWhitespace() }
    val bounds = grid.bounds
    val problems: List<List<String>> = (bounds.left..bounds.right)
      .map { x ->
        (bounds.top..bounds.bottom)
          .map { y ->
            grid[x, y]
          }
      }

    return problems.sumOf { solveProblem(it) }
  }

  fun solveProblem(problem: List<String>): Long {
    val (op, identity) = getOperation(problem.last())
    val normalNumbers = problem.dropLast(1)
    val lastIndex = normalNumbers.maxOf { it.length - 1 }

    d("nums = $normalNumbers")

    val cephalopodNumbers = (lastIndex downTo 0)
      .map { idx ->
        val i: Iterable<Char> = normalNumbers
          .map { it.reversed() }
          .charsAtPosition(idx).filterNotNull()
          .asIterable()
        i.digitsToLong()
      }

    d("ceph = $cephalopodNumbers")

      return cephalopodNumbers
      .fold(identity) {acc, long -> op(acc, long)}
        .also { d("=> $it") }


  }

  private fun getOperation(opString: String): Pair<(Long, Long) -> Long, Long> {
    val op: (Long, Long) -> Long
    val identity: Long

    when(opString) {
      "*" -> {
        op = {a, b -> a * b}
        identity = 1L
      }
      "+" -> {
        op = {a, b -> a + b}
        identity = 0L
      }
      else -> error("")
    }
    return op to identity
  }

}

private fun List<String>.charsAtPosition(idx: Int) : Sequence<Char?> {
  return sequence {
    forEach { s ->
      yield(s.getOrNull(idx))
    }
  }
}


fun main() {
  val numbers = listOf("64", "23", "314")
  println(numbers.charsAtPosition(2).toList())
  println(numbers.charsAtPosition(1).toList())
}

