package dev.jtbw.aoc2025.lib

import kotlin.math.pow

/** yields every possible list of size [size] whose items are one of [items] */
fun <T> allPossibleLists(options: List<T>, size: Int): Sequence<List<T>> {
  val radix = options.size
  val max = radix.toDouble().pow(size.toDouble()).toInt()
  return (0..<max)
    .asSequence()
    .map { it.toString(radix).padStart(size, '0') }
    .map { string -> string.map { char -> options[char.digitToInt()] } }
}

fun <T> List<T>.pairwise(): Sequence<Pair<T, T>> {
  return sequence {
    for (a in 0..lastIndex - 1) {
      for (b in a + 1..lastIndex) {
        yield(get(a) to get(b))
      }
    }
  }
}
