package dev.jtbw.aoc2025.lib.twodeespace

import java.lang.IllegalArgumentException

typealias Grid<T> = List<List<T>>

typealias MutableGrid<T> = List<MutableList<T>>

operator fun <T> Grid<T>.get(x: Int, y: Int) = this[y][x]

operator fun <T> Grid<T>.get(x: Long, y: Long) = this[y.toInt()][x.toInt()]

operator fun <T> Grid<T>.get(offset: Offset) = this[offset.y.toInt()][offset.x.toInt()]

fun <T> Grid<T>.getOrNull(offset: Offset): T? {
  return if (offset in bounds) {
    this[offset.y.toInt()][offset.x.toInt()]
  } else {
    null
  }
}

operator fun <T> MutableGrid<T>.set(x: Int, y: Int, v: T) {
  this[y][x] = v
}

operator fun <T> MutableGrid<T>.set(offset: Offset, v: T) {
  this[offset.y.toInt()][offset.x.toInt()] = v
}

val Grid<*>.width
  get() = this[0].size
val Grid<*>.height
  get() = this.size

fun <T> Grid<T>.indices(rowMajor: Boolean = true): Sequence<Offset> {
  return sequence {
    if (rowMajor) {
      for (y in 0..<height) {
        for (x in 0..<width) {
          yield(Offset(x, y))
        }
      }
    } else {
      for (x in 0..<width) {
        for (y in 0..<height) {
          yield(Offset(x, y))
        }
      }
    }
  }
}

fun <T> Grid<T>.inBounds(x: Int, y: Int): Boolean {
  return y in indices && x in this[y].indices
}

fun <T> Grid<T>.inBounds(x: Long, y: Long): Boolean {
  return y in indices && x in this[y.toInt()].indices
}

fun <T> Grid<T>.inBounds(offset: Offset): Boolean = inBounds(offset.x, offset.y)

fun <T> List<T>.inBounds(i: Int): Boolean = i in indices

val Grid<*>.bounds: Rect
  get() = Rect(Offset(0, 0), Offset(width - 1, height - 1))

fun <T> String.toGrid(transform: (Char) -> T): Grid<T> {
  return split("\n").map { row -> row.map(transform) }
}

fun <T> List<String>.toGrid(transform: (Char) -> T): Grid<T> {
  return map { row -> row.map(transform) }
}

fun String.toGrid(): Grid<Char> {
  return split("\n").map { row -> row.map { it } }
}

fun List<String>.toGrid(): Grid<Char> {
  return map { row -> row.map { it } }
}

fun <T> Grid<T>.toMultilineString(transform: (T) -> String = { it.toString() }): String {
  return joinToString("\n") { it.joinToString("", transform = transform) }
}

/** @param byColumn: if true, (0, 0), (0, 1), (0, 2) ... (1, 0) */
fun <T> Grid<T>.forEachWithOffset(rowMajor: Boolean = true, action: (Offset, T) -> Unit) {
  if (rowMajor) {
    (0..<height).forEach { y -> (0..<width).forEach { x -> action(Offset(x, y), this[x, y]) } }
  } else {
    (0..<width).forEach { x -> (0..<height).forEach { y -> action(Offset(x, y), this[x, y]) } }
  }
}

fun <T> Grid<T>.offsetOfFirst(predicate: (T) -> Boolean): Offset {
  (0..<height).forEach { y ->
    (0..<width).forEach { x ->
      if (predicate(this[x, y])) {
        return Offset(x, y)
      }
    }
  }

  throw IllegalArgumentException("No such element")
}

fun <T> Grid<T>.asSequenceWithOffset(rowMajor: Boolean = true): Sequence<Pair<Offset, T>> {
  val grid = this
  return sequence {
    if (rowMajor) {
      (0..<height).forEach { y -> (0..<width).forEach { x -> yield(Offset(x, y) to grid[x, y]) } }
    } else {
      (0..<width).forEach { x -> (0..<height).forEach { y -> yield(Offset(x, y) to grid[x, y]) } }
    }
  }
}

fun <T> Grid<T>.toMutableGrid(): MutableGrid<T> {
  return this as MutableGrid<T>
}

fun <T> Grid<T>.copy(): Grid<T> = map { row -> row.toList() }
