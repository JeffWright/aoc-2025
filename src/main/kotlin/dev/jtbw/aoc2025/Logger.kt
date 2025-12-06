package dev.jtbw.aoc2025

val DEBUG = true

const val ANSI_RED = "\u001B[31m"
const val ANSI_RESET = "\u001B[0m"

fun i(msg: Any) = println(msg)

fun e(msg: Any) = println("$ANSI_RED$msg$ANSI_RESET")

fun d(msg: Any) =
    if (DEBUG) {
      println(msg)
    } else {}
