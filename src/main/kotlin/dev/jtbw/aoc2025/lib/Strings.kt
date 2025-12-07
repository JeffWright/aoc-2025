package dev.jtbw.aoc2025.lib

/** [123, 456, 789].charsAtPosition(1) => [2, 5, 8] */
fun List<String>.charsAtPosition(idx: Int): Sequence<Char?> {
  return sequence { forEach { s -> yield(s.getOrNull(idx)) } }
}
