package dev.jtbw.aoc2025.lib

fun Iterable<String>.toLongs(): List<Long> = map { it.toLong() }

fun Iterable<String>.toInts(): List<Int> = map { it.toInt() }

fun String.splitWhitespace() = split(Regex("""\s+""")).filterNot { it.isBlank() }

fun String.splitCommas() = split(Regex("""\s*,\s*"""))
