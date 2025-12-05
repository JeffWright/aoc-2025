package dev.jtbw.aoc2025.lib.twodeespace

data class Rect(val topLeft: Offset, val bottomRight: Offset) {
  val top: Long
    get() = topLeft.y

  val bottom: Long
    get() = bottomRight.y

  val left: Long
    get() = topLeft.x

  val right: Long
    get() = bottomRight.x
}

operator fun Rect.contains(pos: Offset): Boolean {
  return pos.x in topLeft.x..bottomRight.x && pos.y in topLeft.y..bottomRight.y
}

fun Rect.offsets(): Sequence<Offset> = sequence {
  (top..bottom).forEach { y -> (left..right).forEach { x -> yield(Offset(x, y)) } }
}
