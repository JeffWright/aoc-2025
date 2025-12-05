package dev.jtbw.aoc2025.lib

import dev.jtbw.aoc2025.lib.twodeespace.Rect

fun interface Traversal<T> {
  fun nexts(current: T): Sequence<T>
}

data class RowMajor(val rect: Rect) {}
