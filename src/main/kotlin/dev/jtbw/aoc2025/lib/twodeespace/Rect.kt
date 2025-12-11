package dev.jtbw.aoc2025.lib.twodeespace

data class Rect(val left: Long, val top: Long, val right: Long, val bottom: Long) {
  constructor(
    corner1: Offset,
    corner2: Offset,
  ) : this(
    top = minOf(corner1.y, corner2.y),
    bottom = maxOf(corner1.y, corner2.y),
    left = minOf(corner1.x, corner2.x),
    right = maxOf(corner1.x, corner2.x),
  )
  constructor(
    corners: Pair<Offset, Offset>
  ) : this(corners.first, corners.second)

  val topLeft
    get() = Offset(left, top)

  val bottomLeft
    get() = Offset(left, bottom)

  val topRight
    get() = Offset(right, top)

  val bottomRight
    get() = Offset(right, bottom)

  val width = right - left
  val height = bottom - top

  override fun toString(): String {
    return "Rect($topLeft-$bottomRight)"
  }
}

operator fun Rect.contains(pos: Offset): Boolean {
  return pos.x in left..right && pos.y in top..bottom
}

fun Rect.walkOffsets(rowMajor: Boolean = true): Sequence<Offset> = sequence {
  if (rowMajor) {
    (top..bottom).forEach { y -> (left..right).forEach { x -> yield(Offset(x, y)) } }
  } else {
    (left..right).forEach { y -> (top..bottom).forEach { x -> yield(Offset(x, y)) } }
  }
}

fun Rect.sides(): Sequence<LineSegment> {
  return sequence {
    yield(LineSegment(topLeft, topRight))
    yield(LineSegment(topRight, bottomRight))
    yield(LineSegment(bottomRight, bottomLeft))
    yield(LineSegment(bottomLeft, topLeft))
  }
}

/**
 * @param tileBased if true, treat coordinates as tiles on a grid instead of points in space. E.g.
 *   (0,0)-(2,2) would like
 * <pre>
 *   000
 *   000
 *   000
 *  </pre>
 *
 * therefore area = 9
 */
fun Rect.area(tileBased: Boolean = false): Long {
  return if (tileBased) {
    (width + 1) * (height + 1)
  } else {
    width * height
  }
}

/**
 * Shrinks the rect by x on left & right, and y on top & bottom so the width will decrease by 2x,
 * and the height by 2y
 */
fun Rect.shrinkBy(x: Int, y: Int): Rect {
  require(width >= 2 * x)
  require(height >= 2 * y)

  return Rect(top = top + y, bottom = bottom - y, left = left + x, right = right - x)
}
