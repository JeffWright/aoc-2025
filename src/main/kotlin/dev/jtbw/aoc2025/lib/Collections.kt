package dev.jtbw.aoc2025.lib

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

fun <T> List<T>.countOf(item: T): Int = count { it == item }

operator fun <E> MutableList<E>.set(indices: IntRange, value: E) {
  indices.forEach { this[it] = value }
}

operator fun <E> MutableList<E>.set(indices: Iterable<Int>, value: E) {
  indices.forEach { this[it] = value }
}

fun <T> MutableMap<T, Int>.increment(key: T) {
  this[key] = getOrDefault(key, 0)
}
