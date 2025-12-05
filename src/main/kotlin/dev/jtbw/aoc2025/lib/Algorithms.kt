package dev.jtbw.aoc2025.lib

import dev.jtbw.aoc2025.lib.twodeespace.Offset

/* Credit: https://rosettacode.org/wiki/Shoelace_formula_for_polygonal_area */
fun shoelaceArea(v: List<Offset>): Double {
  val n = v.size
  var a = 0.0
  for (i in 0..<n - 1) {
    require(v[i].x == v[i + 1].x || v[i].y == v[i + 1].y)
    a += v[i].x * v[i + 1].y - v[i + 1].x * v[i].y
  }
  require(v[0].x == v[n - 1].x || v[0].y == v[n - 1].y)
  return Math.abs(a + v[n - 1].x * v[0].y - v[0].x * v[n - 1].y) / 2.0
}
