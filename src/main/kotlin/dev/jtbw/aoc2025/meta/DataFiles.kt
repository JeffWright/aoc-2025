package dev.jtbw.aoc2025.meta

import java.io.File

object DataFiles {
  private val year = 2025
  private val root = File("src/main/resources/$year")

  fun f(day: Int, name: String) = File(File(root, "day$day"), name)

  fun sampleInput(day: Int): File {
    return f(day, "sample.input")
  }

  fun realInput(day: Int): File {
    return f(day, "main.input")
  }

  fun sample1Answer(day: Int): File {
    return f(day, "part1_sample.answer")
  }

  fun sample2Answer(day: Int): File {
    return f(day, "part2_sample.answer")
  }

  fun realAnswer(day: Int, part: Int): File {
    return f(day, "part$part.answer")
  }
}
