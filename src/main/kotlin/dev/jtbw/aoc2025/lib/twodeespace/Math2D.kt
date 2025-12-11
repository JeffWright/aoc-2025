package dev.jtbw.aoc2025.lib.twodeespace

enum class Orientation {
  Clockwise,
  CounterClockwise,
  Colinear,
}

/** Credit: https://www.geeksforgeeks.org/dsa/check-if-two-given-line-segments-intersect/ */
fun orientation(p: Offset, q: Offset, r: Offset): Orientation {
  val o = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y)

  return when {
    o == 0L -> Orientation.Colinear
    o > 0 -> Orientation.Clockwise
    else -> Orientation.CounterClockwise
  }
}

fun onSegment(p: Offset, lineSegment: LineSegment): Boolean {
  if (orientation(p, lineSegment.start, lineSegment.end) != Orientation.Colinear) {
    return false
  }

  return p in lineSegment.boundingRect()
}

/** Credit: https://www.geeksforgeeks.org/dsa/check-if-two-given-line-segments-intersect/ */
fun LineSegment.intersects(other: LineSegment): Boolean {
  val l1 = this
  val l2 = other
  val o1 = orientation(l1.start, l1.end, l2.start)
  val o2 = orientation(l1.start, l1.end, l2.end)
  val o3 = orientation(l2.start, l2.end, l1.start)
  val o4 = orientation(l2.start, l2.end, l1.end)

  if (o1 != o2 && o3 != o4) return true

  if (o1 == Orientation.Colinear && onSegment(l2.start, LineSegment(l1.start, l1.end))) return true

  if (o2 == Orientation.Colinear && onSegment(l2.end, LineSegment(l1.start, l1.end))) return true

  if (o3 == Orientation.Colinear && onSegment(l1.start, LineSegment(l2.start, l2.end))) return true

  if (o4 == Orientation.Colinear && onSegment(l1.end, LineSegment(l2.start, l2.end))) return true

  return false
}

fun LineSegment.intersects(rect: Rect): Boolean {
  return rect.sides().any { intersects(it) }
}
