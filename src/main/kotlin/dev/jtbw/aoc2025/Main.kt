package dev.jtbw.aoc2025

import com.google.gson.GsonBuilder
import java.io.File

fun main() {
  clearResults()

  Day1.run()
  Day2.run()
  Day3.run()
  Day4.run()
  Day5.run()

  writeMetrics()
}

fun writeMetrics() {
  val metricsFile = File("metrics.json")

  // Group results by day
  val metricsByDay = dayResults.groupBy { it.day }.mapValues { (_, results) ->
    mapOf(
      "part1" to results.find { it.part == 1 }?.let {
        mapOf("runtime_ms" to it.runtime.inWholeMilliseconds, "success" to it.success)
      },
      "part2" to results.find { it.part == 2 }?.let {
        mapOf("runtime_ms" to it.runtime.inWholeMilliseconds, "success" to it.success)
      }
    ).filterValues { it != null }
  }

  val metrics = mapOf(
    "year" to year,
    "days" to metricsByDay,
    "total_stars" to dayResults.count { it.success },
    "total_days" to metricsByDay.size,
    "last_updated" to java.time.Instant.now().toString()
  )

  val gson = GsonBuilder().setPrettyPrinting().create()
  metricsFile.writeText(gson.toJson(metrics))
  i("Metrics written to ${metricsFile.absolutePath}")
}
