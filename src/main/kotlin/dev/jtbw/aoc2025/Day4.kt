package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.lib.twodeespace.Grid
import dev.jtbw.aoc2025.lib.twodeespace.Offset
import dev.jtbw.aoc2025.lib.twodeespace.bounds
import dev.jtbw.aoc2025.lib.twodeespace.getOrNull
import dev.jtbw.aoc2025.lib.twodeespace.neighbors
import dev.jtbw.aoc2025.lib.twodeespace.offsets
import dev.jtbw.aoc2025.lib.twodeespace.set
import dev.jtbw.aoc2025.lib.twodeespace.toGrid
import dev.jtbw.aoc2025.lib.twodeespace.toMutableGrid

object Day4 {
  val sample =
    """
    ..@@.@@@@.
    @@@.@.@.@@
    @@@@@.@.@@
    @.@@@@..@.
    @@.@@@@.@@
    .@@@@@@@.@
    .@.@.@.@@@
    @.@@@.@@@@
    .@@@@@@@@.
    @.@.@@@.@.
    """
      .trimIndent()

  fun run() {
    val day = 4
    sample(sample, "13", ::part1)
    day(day, 1, ::part1)

    sample(sample, "43", ::part2)
    day(day, 2, ::part2)
  }

  suspend fun part1(input: String): Any {
    val grid = input.lines().toGrid { Tile.from(it) }

    return grid.bounds.offsets().count { offset ->
      grid.getOrNull(offset) == Tile.PAPER && countNeighbors(grid, offset) < 4
    }
  }

  suspend fun part2(input: String): Any {
    val grid = input.lines().toGrid { Tile.from(it) }.toMutableGrid()
    val queue = mutableSetOf<Offset>()
    var removed = 0

    // Prime the queue with all accessible tiles
    grid.bounds
      .offsets()
      .filter { grid.getOrNull(it) == Tile.PAPER }
      .forEach { offset ->
        countNeighbors(grid, offset).also { neighbors ->
          if (neighbors < 4) {
            queue.add(offset)
          }
        }
      }

    while (queue.isNotEmpty()) {
      // Pop next
      val offset = queue.first()
      queue.remove(offset)

      // Remove paper here
      removed++
      grid[offset] = Tile.EMPTY

      // Add to the queue any neighbors that are newly accessible
      offset.neighbors().forEach { neighbor ->
        if (grid.getOrNull(neighbor) == Tile.PAPER && countNeighbors(grid, neighbor) < 4) {
          queue.add(neighbor)
        }
      }
    }

    return removed
  }

  private fun countNeighbors(grid: Grid<Tile>, offset: Offset): Int {
    return offset.neighbors().count { grid.getOrNull(it) == Tile.PAPER }
  }

  private enum class Tile {
    PAPER,
    EMPTY;

    companion object {
      fun from(c: Char): Tile {
        return if (c == '@') PAPER else EMPTY
      }
    }
  }
}
