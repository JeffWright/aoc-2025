package dev.jtbw.aoc2025.lib.twodeespace

import kotlin.math.sqrt

data class Offset3(val x: Long, val y: Long, val z: Long) {
  constructor(x: Int, y: Int, z: Int) : this(x.toLong(), y.toLong(), z.toLong())

  override fun toString(): String {
    return "($x, $y, $z)"
  }
}

operator fun Offset3.plus(other: Offset3): Offset3 {
  return Offset3(x + other.x, y + other.y, z + other.z)
}

operator fun Offset3.minus(other: Offset3): Offset3 {
  return Offset3(x - other.x, y - other.y, z - other.z)
}

operator fun Offset3.times(other: Int): Offset3 {
  return Offset3(x * other, y * other, z * other)
}

fun Offset3.distanceTo(other: Offset3): Double {
  val dx = other.x - x
  val dy = other.y - y
  val dz = other.z - z
  return sqrt((dx * dx + dy * dy + dz * dz).toDouble())
}

/** Skip the sqrt (if you only care about comparing relative distances) */
fun Offset3.fastDistanceTo(other: Offset3): Double {
  val dx = other.x - x
  val dy = other.y - y
  val dz = other.z - z
  return ((dx * dx + dy * dy + dz * dz).toDouble())
}
