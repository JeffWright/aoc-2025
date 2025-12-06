package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.meta.AdventOfCodeClient
import dev.jtbw.aoc2025.meta.AnswerResponse
import dev.jtbw.aoc2025.meta.DataFiles
import dev.jtbw.aoc2025.meta.InputAndAnswer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.File
import kotlin.time.Duration
import kotlin.time.measureTime

class AoCTests :
    FunSpec({
      failfast = true
      println(File(".").absolutePath)
      var abort = false
      (1..12)
        .filter { !abort }
          .filter { day -> File("src/main/kotlin/dev/jtbw/aoc2025/Day$day.kt").exists() }
          .forEach { day ->
            val data = DataFiles.inputAndAnswers(day)
            val obj = Class.forName("dev.jtbw.aoc2025.Day$day").kotlin.objectInstance as AoCDay

              test("Day $day") {
                  runDay(obj, data)
            }
          }
    })

suspend fun runDay(day: AoCDay, data: List<InputAndAnswer>) {
data.forEach {
  runTest(day, it)
}
}

suspend fun runTest(day: AoCDay, data: InputAndAnswer) {
  println(data.name)
  val client = AdventOfCodeClient()

  val f = when(data.part) {
    1 -> day::part1
    2 -> day::part2
    else -> error("")
  }

  val input = client.fetchInput(data)
  val answer: Any
  val runtime = measureTime {
    answer = f(input)
  }

  val correctAnswer = data.getCorrectAnswer()
  if(correctAnswer != null) {
    answer.toString() shouldBe correctAnswer
  } else {
    client.checkAnswer(data, answer.toString()) shouldBe AnswerResponse.Correct
  }

  val result = DayResult(data.name, runtime)
  println(result)
}
data class DayResult(val name: String, val runtime: Duration)
