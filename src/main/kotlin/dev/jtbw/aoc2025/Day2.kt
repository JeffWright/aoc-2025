package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.lib.distinctUntilChanged
import dev.jtbw.aoc2025.lib.nextOrNull
import dev.jtbw.aoc2025.lib.repeat
import kotlin.math.absoluteValue
import kotlin.math.log10
import kotlin.math.pow

object Day2 : AoCDay {
  override suspend fun part1(input: String): String {
    val ranges =
      input
        .trim()
        .split(",")
        .map { it.substringBefore("-").toLong()..it.substringAfter("-").toLong() }
        .sortedBy { it.first }
        .asSequence()

    return ranges
      .flatMap { range ->
        val numDigits = range.first.numDigits()
        val evenNumDigits = numDigits.rem(2) == 0

        val subnumStart =
          when {
            evenNumDigits -> range.first.slice(0..<(numDigits / 2))
            else -> 10.0.pow(numDigits / 2).toLong()
          }

        (subnumStart..Long.MAX_VALUE)
          .asSequence()
          .map { it.repeat(2) }
          .takeWhile { it <= range.last }
          .filter { it in range }
      }
      .sum()
      .toString()
  }

  override suspend fun part2(input: String): String {
    val ranges =
      input
        .trim()
        .split(",")
        .map { it.substringBefore("-").toLong()..it.substringAfter("-").toLong() }
        .sortedBy { it.first }
        .asSequence()

    return ranges.flatMap { range -> invalidIdsIn(range) }.sum().toString()
  }

  /** Emits all invalid IDs that are within [range] */
  private fun invalidIdsIn(range: LongRange): Sequence<Long> {
    val minDigits = range.first.numDigits()
    val maxDigits = range.last.numDigits()

    val iterators = buildList {
      (minDigits..maxDigits).forEach { numDigits ->
        (1..numDigits / 2).forEach { subdigits ->
          add(invalidIds(digits = numDigits, subdigits = subdigits, min = range.first).iterator())
        }
      }
    }

    return SortedIteratorSet(iterators)
      .asSequence()
      .distinctUntilChanged()
      .dropWhile { it < range.first }
      .takeWhile { it <= range.last }
  }

  /**
   * Emits all invalid IDs that are exactly [digits] digits long AND are constructable by repeating
   * a substring of length [subdigits] AND are >= min
   */
  private fun invalidIds(digits: Int, subdigits: Int, min: Long): Sequence<Long> {
    if (digits.rem(subdigits) != 0 || subdigits > digits / 2) {
      return emptySequence()
    }
    val repeats = digits / subdigits
    val subnumEnd = 10.0.pow(subdigits).toLong()
    return sequence {
      val subnumStart =
        if (min.numDigits() < digits) {
          10.0.pow(subdigits - 1).toLong()
        } else {
          min.slice(0..<(subdigits))
        }
      (subnumStart..<subnumEnd).forEach { sub -> yield(sub.repeat(repeats)) }
    }
  }
}

/** An iterator that returns all values from all given iterators in sorted order */
class SortedIteratorSet(val iterators: List<Iterator<Long>>) : Iterator<Long> {
  val nexts = iterators.indices.associateWith { iterators[it].nextOrNull() }.toMutableMap()

  override fun next(): Long {
    val entry: Map.Entry<Int, Long?> = nexts.minBy { (k, v) -> v ?: Long.MAX_VALUE }
    val n = entry.value!!
    nexts[entry.key] = iterators[entry.key].nextOrNull()
    return n
  }

  override fun hasNext(): Boolean {
    return nexts.any { it.value != null }
  }
}

fun Long.numDigits(): Int {
  if (this == 0L) return 1
  return log10(this.absoluteValue.toDouble()).toInt() + 1
}

fun Long.slice(range: IntRange): Long {
  // Who needs math when you have toString?
  return toString().slice(range).toLong()
}
