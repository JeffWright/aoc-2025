package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.meta.AdventOfCodeClient
import dev.jtbw.aoc2025.meta.AnswerResponse
import dev.jtbw.aoc2025.meta.DataFiles
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

val client = AdventOfCodeClient()

class Day8Tests :
  FunSpec({
    failfast = true
    val day = 8
    test("Day $day sample") {
      val input = DataFiles.sampleInput(day).readText()
      val answer = Day8.part1(input, 10)
      answer.toString() shouldBe DataFiles.sample1Answer(day).readText()
    }
    test("Day $day part 1") {
      val input = client.fetchInput(2025, day)
      val answer = Day8.part1(input)
      client.checkAnswer(answer.toString(), 2025, day, 1).shouldBe(AnswerResponse.Correct)
    }
    test("Day $day sample 2") {
      val input = DataFiles.sampleInput(day).readText()
      val answer = Day8.part2(input)
      answer.toString() shouldBe DataFiles.sample2Answer(day).readText()
    }
    test("Day $day part 2") {
      val input = client.fetchInput(2025, day)
      val answer = Day8.part2(input)
      client.checkAnswer(answer.toString(), 2025, day, 2).shouldBe(AnswerResponse.Correct)
    }
  })
