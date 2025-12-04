package dev.jtbw.aoc2025

object DayTemplate {
  val sample =
    """
    """
      .trimIndent()

  fun run() {
    val day = TODO()
    sample(sample, "", ::part1)
    day(day, 1, ::part1)

    sample(sample, "", ::part2)
    day(day, 2, ::part2)
  }

  suspend fun part1(input: String): Any {
    TODO()
  }

  suspend fun part2(input: String): Any {
    TODO()
  }
}
