package dev.jtbw.aoc2025.meta

import com.google.gson.Gson
import java.io.File

const val NUM_DAYS = 12

data class PartMetrics(val runtime_ms: Long, val success: Boolean)

data class DayMetrics(val part1: PartMetrics?, val part2: PartMetrics?)

data class Metrics(
    val year: Int,
    val days: Map<String, DayMetrics>,
    val total_stars: Int,
    val total_days: Int,
    val last_updated: String,
)

fun main() {
  generateStatus()
}

fun generateStatus() {
  val metricsFile = File("metrics.json")
  if (!metricsFile.exists()) {
    println("metrics.json not found.")
    return
  }

  val gson = Gson()
  val metrics = gson.fromJson(metricsFile.readText(), Metrics::class.java)

  val readme = buildString {
    appendLine("# 🎄 Advent of Code ${metrics.year}")
    appendLine()
    appendLine(
        "![](https://img.shields.io/badge/⭐_Stars-${metrics.total_stars}/${NUM_DAYS*2}-brightgreen)"
    )
    appendLine()
    appendLine("## Runtimes")
    appendLine()
    appendLine("| Day | Part 1 | Part 2 |")
    appendLine("|-----|--------|--------|")

    (1..NUM_DAYS).forEach { day ->
      val dayKey = day.toString()
      val dayMetrics = metrics.days[dayKey]
      val dayString = day.toString().padStart(2, '0')
      val dayLink = "[${dayString}](src/main/kotlin/dev/jtbw/aoc2025/Day${day}.kt)"

      val part1Badge =
          dayMetrics?.part1?.let {
            val runtime = dayMetrics.part1.runtime_ms
            val color = getRuntimeColor(runtime)
            "![](https://img.shields.io/badge/${runtime}ms-${color})"
          } ?: "![](https://img.shields.io/badge/pending-lightgrey)"

      val part2Badge =
          dayMetrics?.part2?.let {
            val runtime = dayMetrics.part2.runtime_ms
            val color = getRuntimeColor(runtime)
            "![](https://img.shields.io/badge/${runtime}ms-${color})"
          } ?: "![](https://img.shields.io/badge/pending-lightgrey)"

      appendLine("| $dayLink | $part1Badge | $part2Badge |")
    }

    appendLine()
    appendLine("---")
    appendLine()
    appendLine("Last updated: ${metrics.last_updated}*")
  }

  val readmeFile = File("README.md")
  readmeFile.writeText(readme)
}

fun getRuntimeColor(ms: Long): String {
  return when {
    ms < 10 -> "brightgreen"
    ms < 50 -> "green"
    ms < 100 -> "yellowgreen"
    ms < 200 -> "yellow"
    ms < 500 -> "orange"
    else -> "red"
  }
}
