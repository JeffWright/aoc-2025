package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.lib.pairwise
import dev.jtbw.aoc2025.lib.splitCommas
import dev.jtbw.aoc2025.lib.toLongs
import dev.jtbw.aoc2025.lib.twodeespace.LineSegment
import dev.jtbw.aoc2025.lib.twodeespace.Offset
import dev.jtbw.aoc2025.lib.twodeespace.Rect
import dev.jtbw.aoc2025.lib.twodeespace.area
import dev.jtbw.aoc2025.lib.twodeespace.intersects
import dev.jtbw.aoc2025.lib.twodeespace.shrinkBy

object Day9 {

  fun part1(input: String): Any {
    val redTiles =
      input.lines().map {
        val (x, y) = it.splitCommas().toLongs()
        Offset(x, y)
      }

    return redTiles.pairwise().maxOf { (a, b) -> Rect(a, b).area(tileBased = true) }
  }

  fun part2(input: String): Any {
    val redTiles =
      input.lines().map {
        val (x, y) = it.splitCommas().toLongs()
        Offset(x, y)
      }

    val lineSegments =
      (redTiles.asSequence() + redTiles.first())
        .windowed(2, 1)
        .map { (a, b) -> LineSegment(a, b) }
        .toList()

    return redTiles
      .pairwise()
      .map(::Rect)
      .filter { rect ->
        val sx = rect.width >= 2
        val sy = rect.height >= 2
        val shrunk = rect.shrinkBy(x = if (sx) 1 else 0, y = if (sy) 1 else 0)
        lineSegments.none { it.intersects(shrunk) }
      }
      .maxOf { it.area(tileBased = true) }
  }
}
