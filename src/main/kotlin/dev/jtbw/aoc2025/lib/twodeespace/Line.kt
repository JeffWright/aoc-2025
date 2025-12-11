package dev.jtbw.aoc2025.lib.twodeespace

data class LineSegment(val start: Offset, val end: Offset) {
  constructor(
    startX: Long,
    startY: Long,
    endX: Long,
    endY: Long,
  ) : this(Offset(startX, startY), Offset(endX, endY))
  constructor(
    points: Pair<Offset, Offset>
  ) : this(points.first, points.second)
}

fun LineSegment.boundingRect(): Rect {
  return Rect(start, end)
}
