package dev.jtbw.aoc2025

import kotlin.math.absoluteValue

object Day1 : AoCDay {

  override suspend fun part1(input: String): String {
    val input = input.lines()

    var dial = 50
    var zeroes = 0
    input.forEach { i ->
      val dir = if (i.startsWith("L")) -1 else 1
      dial += i.drop(1).toInt() * dir
      dial = dial.mod(100)
      if (dial == 0) {
        zeroes++
      }
    }

    return zeroes.toString()
  }

  override suspend fun part2(input: String): String {
    val input = input.lines()

    var dial = 50
    var zeroes = 0
    input.forEach { i ->
      val dir = if (i.startsWith("L")) -1 else 1
      val startedOnZero = dial == 0
      dial += i.drop(1).toInt() * dir

      when {
        dial == 0 -> zeroes++
        dial >= 100 -> zeroes += dial / 100
        dial < 0 -> zeroes += dial.absoluteValue / 100 + (if (startedOnZero) 0 else 1)
      }

      dial = dial.mod(100)
    }

    return zeroes.toString()
  }
}
