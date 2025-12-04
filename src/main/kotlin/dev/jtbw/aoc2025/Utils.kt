package dev.jtbw.aoc2025

import kotlin.math.pow

fun Long.repeat(times: Int): Long {
  val len = numDigits()
  var n = this
  repeat(times - 1) {
    n = (n * 10.0.pow(len)).toLong()
    n += this
  }
  return n
}

fun <T : Any> Sequence<T>.distinctUntilChanged(): Sequence<T> {
  val seq = this
  return sequence {
    var last: T? = null
    seq.forEach { next ->
      if (next != last) {
        yield(next)
        last = next
      }
    }
  }
}

fun <T> Iterator<T>.nextOrNull(): T? {
  return if (hasNext()) {
    next()
  } else {
    null
  }
}

/** Returns the 'largest' (by [block]) [n] items, in sorted order (sorted by value) */
fun <T, R : Comparable<R>> Iterable<T>.multiMaxBy(n: Int, block: (T) -> R): List<IndexedValue<T>> {
  val iter = iterator()
  var i = 0
  val q = ArrayDeque<IndexedValue<T>>(n)
  repeat(n) {
    if (iter.hasNext()) {
      q.add(IndexedValue(i++, iter.next()))
    }
  }
  q.sortBy { (_, item) -> block(item) }

  iter.forEach { item ->
    val last = q.last()
    if (block(item) > block(last.value)) {
      q.addLast(IndexedValue(i, item))
      if (q.size > n) {
        q.removeFirst()
      }
    }
    i++
  }

  return q
}

data class IndexedValue<T>(val idx: Int, val value: T)
