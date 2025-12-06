package dev.jtbw.aoc2025.lib

/** [1, 2, 3] -> 123 */
fun Iterable<Char>.digitsToLong(): Long {
  return fold(0L) { acc, i -> (acc * 10) + i.digitToInt() }
}

///** [1, 2, 3] -> 123 */
//fun Iterable<Int>.digitsToLong(): Long {
//  return fold(0L) { acc, i -> (acc * 10) + i }
//}
