package dev.jtbw.aoc2025.lib

import dev.jtbw.aoc2025.numDigits
import kotlin.math.pow

/** 12.repeat(3) = 121212 */
fun Long.repeat(times: Int): Long {
  val len = numDigits()
  var n = this
  repeat(times - 1) {
    n = (n * 10.0.pow(len)).toLong()
    n += this
  }
  return n
}

data class IndexedValue<T>(val idx: Int, val value: T)

fun <T> Sequence<T>.repeatForever(): Sequence<T> {
  val src = this
  return sequence {
    while (true) {
      yieldAll(src)
    }
  }
}
