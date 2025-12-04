package dev.jtbw.aoc2025

import kotlin.system.exitProcess
import kotlin.time.measureTime
import kotlinx.coroutines.runBlocking

val year = 2025
val submit = true

fun day(day: Int, part: Int, block: suspend (String) -> Any) {
  runBlocking {
    AdventOfCodeClient().use { client ->
      val input = client.fetchInput(2025, day)
      val answer: String
      val time = measureTime { answer = block(input).toString() }
      val response = client.submitAnswer(year, day, part, answer)
      if (response == AnswerResponse.Correct) {
        i("✅ Day $day part $part is CORRECT! in $time")
      } else {
        e("❌ Answer $answer is wrong! $response")
        exitProcess(0)
      }
    }
  }
}

fun sample(input: String, correctAnswer: String, block: suspend (String) -> Any) {
  runBlocking {
    val answer: String
    val time = measureTime { answer = block(input).toString() }
    if (answer == correctAnswer) {
      i("✅ Sample => $correctAnswer is CORRECT! in $time")
    } else {
      e("❌ Answer $answer is wrong! Should be $correctAnswer")
      exitProcess(0)
    }
  }
}
