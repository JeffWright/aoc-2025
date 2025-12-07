package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.lib.traversal
import dev.jtbw.aoc2025.lib.twodeespace.Direction.Orthogonal.LEFT
import dev.jtbw.aoc2025.lib.twodeespace.Direction.Orthogonal.RIGHT
import dev.jtbw.aoc2025.lib.twodeespace.Grid
import dev.jtbw.aoc2025.lib.twodeespace.Offset
import dev.jtbw.aoc2025.lib.twodeespace.asSequenceWithOffset
import dev.jtbw.aoc2025.lib.twodeespace.bounds
import dev.jtbw.aoc2025.lib.twodeespace.get
import dev.jtbw.aoc2025.lib.twodeespace.offsetOfFirst
import dev.jtbw.aoc2025.lib.twodeespace.plus
import dev.jtbw.aoc2025.lib.twodeespace.toGrid
import dev.jtbw.aoc2025.lib.twodeespace.toMutableGrid
import kotlin.collections.firstOrNull

object Day7 : AoCDay {

  override suspend fun part1(input: String): Any {
    val grid = input.trim().toGrid { Tile.from(it) }.toMutableGrid()
    val start = raytrace(grid, start = grid.offsetOfFirst { it == Tile.START })!!

    return traversal(start, skipRepeats = true) { splitter ->
        visit(splitter)
        for (side in leftRight) {
          raytrace(grid, start = splitter + side)?.let { child -> visit(child) }
        }
      }
      .count()
  }

  override suspend fun part2(input: String): Any {
    val grid = input.trim().toGrid { Tile.from(it) }.toMutableGrid()
    val start = raytrace(grid, start = grid.offsetOfFirst { it == Tile.START })!!
    val numPathsTo = mutableMapOf<Offset, Long>()
    numPathsTo[start] = 1

    val splittersTopToBottom =
      grid.asSequenceWithOffset().filter { it.second == Tile.SPLITTER }.map { it.first }

    var sum = 0L
    splittersTopToBottom.forEach { splitter ->
      val pathsHere = numPathsTo[splitter] ?: 0L

      for (side in leftRight) {
        val next = raytrace(grid, start = splitter + side)
        if (next == null) {
          // Reached the bottom, record the number
          sum += pathsHere
        } else {
          // Add our tally to child's tally
          numPathsTo[next] = numPathsTo.getOrDefault(next, 0) + pathsHere
        }
      }
    }

    return sum
  }

  private val leftRight = listOf(LEFT, RIGHT)

  private fun raytrace(grid: Grid<Tile>, start: Offset): Offset? {
    val x = start.x
    val path = start.y..grid.bounds.bottom
    return path.firstOrNull { y -> grid[x, y] == Tile.SPLITTER }?.let { Offset(x, it) }
  }

  enum class Tile(val ch: Char) {
    EMPTY('.'),
    START('S'),
    SPLITTER('^');

    companion object {
      fun from(ch: Char) =
        when (ch) {
          'S' -> START
          '^' -> SPLITTER
          else -> EMPTY
        }
    }
  }
}
