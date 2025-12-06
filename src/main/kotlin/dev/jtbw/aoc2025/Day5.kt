package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.lib.overlaps
import dev.jtbw.aoc2025.lib.plus
import dev.jtbw.aoc2025.lib.size
import dev.jtbw.aoc2025.lib.toLongRange

object Day5 : AoCDay {

  override suspend fun part1(input: String): Any {
    val (rawRanges, rawIds) = input.split("\n\n")
    val ranges = rawRanges.lines().map { it.toLongRange() }

    val ids = rawIds.lines().map { it.toLong() }
    return ids.count { id -> ranges.any { id in it } }
  }

  override suspend fun part2(input: String): Any {
    val (rawRanges) = input.split("\n\n")
    val ranges = rawRanges.lines().map { it.toLongRange() }.sortedBy { it.first }

    // Sort the ranges, merge any that overlap (to avoid double-counting), then
    // just sum up their sizes.

    return sequence {
          var thisRange = ranges.first()
          ranges.drop(1).forEach { nextRange ->
            if (thisRange.overlaps(nextRange)) {
              // merge nextRange into thisRange
              thisRange += nextRange
            } else {
              // emit thisRange and move on
              yield(thisRange)
              thisRange = nextRange
            }
          }
          yield(thisRange)
        }
        .sumOf { it.size }
  }
}
