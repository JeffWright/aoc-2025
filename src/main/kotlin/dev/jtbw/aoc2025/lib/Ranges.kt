package dev.jtbw.aoc2025.lib

/** Same as this..other or other..this, depending on which is larger */
fun Int.ascendingRangeTo(other: Int) = minOf(this, other)..maxOf(this, other)

/** Same as this..other or other..this, depending on which is larger */
fun Long.ascendingRangeTo(other: Long) = minOf(this, other)..maxOf(this, other)

fun String.toLongRange(delimiter: String = "-"): LongRange {
  val (start, end) = this.split(delimiter)
  return start.toLong()..end.toLong()
}

operator fun LongRange.plus(other: LongRange): LongRange {
  require(overlaps(other))
  return minOf(first, other.first)..maxOf(last, other.last)
}

fun <T : Comparable<T>> ClosedRange<T>.overlaps(other: ClosedRange<T>): Boolean {
  return other.start in this || other.endInclusive in this
}

val LongRange.size
  get() = last - first + 1
