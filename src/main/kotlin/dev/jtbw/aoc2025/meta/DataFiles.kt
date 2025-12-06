package dev.jtbw.aoc2025.meta

import java.io.File

object DataFiles {
  private val year = 2025
  private val root = File("src/main/resources/$year")
  fun inputAndAnswers(day: Int): List<InputAndAnswer> {

    fun f(name: String) = File(File(root, "day$day"), name)

    return listOf(
      InputAndAnswer(
        name = "Part 1 Sample",
        year = year,
        day = day,
        part = 1,
        primary = false,
        inputFile = f("sample.input"),
        answerFile = f("part1_sample.answer"),
      ),
      InputAndAnswer(
        name = "Part 1",
        year = year,
        day = day,
        part = 1,
        primary = true,
        inputFile = f("main.input"),
        answerFile = f("part1.answer"),
      ),
      InputAndAnswer(
        name = "Part 2 Sample",
        year = year,
        day = day,
        part = 2,
        primary = false,
        inputFile = f("sample.input"),
        answerFile = f("part2_sample.answer"),
      ),
      InputAndAnswer(
        name = "Part 2",
        year = year,
        day = day,
        part = 2,
        primary = true,
        inputFile = f("main.input"),
        answerFile = f("part2.answer"),
      ),
    )
  }

  fun inputFile(day: Int) = inputAndAnswers(day).first().inputFile
}

data class InputAndAnswer(
  val name: String,
  val year: Int,
  val day: Int,
  val part: Int,
  val primary: Boolean,
  val inputFile: File,
  val answerFile: File
) {
  fun recordCorrectAnswer(answer: String) {
    require(!answerFile.exists()) { "Answer file already exists!" }
    answerFile.writeText(answer)
  }

  fun getInput(): String? {
    return inputFile.takeIf { it.exists() }
      ?.readText()
  }

  fun recordInput(input: String) {
    inputFile.parentFile.mkdirs()
    inputFile.writeText(input)
  }

  fun getCorrectAnswer(): String? {
    return answerFile.takeIf { it.exists() }
      ?.readText()?.trim()
  }

}