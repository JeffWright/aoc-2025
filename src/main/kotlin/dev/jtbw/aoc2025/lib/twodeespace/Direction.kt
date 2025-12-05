package dev.jtbw.aoc2025.lib.twodeespace

import dev.jtbw.aoc2025.lib.twodeespace.Axis.HORIZONTAL
import dev.jtbw.aoc2025.lib.twodeespace.Axis.VERTICAL
import dev.jtbw.aoc2025.lib.twodeespace.Direction.Diagonal
import dev.jtbw.aoc2025.lib.twodeespace.Direction.Diagonal.*
import dev.jtbw.aoc2025.lib.twodeespace.Direction.Orthogonal
import dev.jtbw.aoc2025.lib.twodeespace.Direction.Orthogonal.*

sealed interface Direction {
  sealed interface Orthogonal : Direction {
    data object UP : Orthogonal

    data object DOWN : Orthogonal

    data object LEFT : Orthogonal

    data object RIGHT : Orthogonal
  }

  sealed interface Diagonal : Direction {
    data object UPLEFT : Diagonal

    data object UPRIGHT : Diagonal

    data object DOWNLEFT : Diagonal

    data object DOWNRIGHT : Diagonal
  }

  companion object {
    val orthogonals = listOf(UP, DOWN, LEFT, RIGHT)
    val diagonals = listOf(UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT)
    val all = orthogonals + diagonals
  }
}

val Orthogonal.opposite: Orthogonal
  get() =
    when (this) {
      DOWN -> UP
      LEFT -> RIGHT
      RIGHT -> LEFT
      UP -> DOWN
    }

val Orthogonal.isHorizontal: Boolean
  get() = this == LEFT || this == RIGHT

val Orthogonal.isVertical: Boolean
  get() = this == UP || this == DOWN

val Direction.offset: Offset
  get() {
    return when (this) {
      DOWN -> Offset(0, 1)
      DOWNLEFT -> Offset(-1, 1)
      DOWNRIGHT -> Offset(1, 1)
      LEFT -> Offset(-1, 0)
      RIGHT -> Offset(1, 0)
      UP -> Offset(0, -1)
      UPLEFT -> Offset(-1, -1)
      UPRIGHT -> Offset(1, -1)
    }
  }

fun Orthogonal.rotateLeft() =
  when (this) {
    UP -> LEFT
    LEFT -> DOWN
    DOWN -> RIGHT
    RIGHT -> UP
  }

fun Orthogonal.rotateRight() =
  when (this) {
    UP -> RIGHT
    RIGHT -> DOWN
    DOWN -> LEFT
    LEFT -> UP
  }

fun Orthogonal.rotate90(clockwise: Boolean) = if (clockwise) rotateRight() else rotateLeft()

val Diagonal.opposite: Diagonal
  get() =
    when (this) {
      DOWNLEFT -> UPRIGHT
      DOWNRIGHT -> UPLEFT
      UPLEFT -> DOWNRIGHT
      UPRIGHT -> DOWNLEFT
    }

val Direction.opposite: Direction
  get() =
    when (this) {
      is Orthogonal -> opposite
      is Diagonal -> opposite
    }

fun Diagonal.rotateRight() =
  when (this) {
    DOWNLEFT -> UPLEFT
    UPLEFT -> UPRIGHT
    UPRIGHT -> DOWNRIGHT
    DOWNRIGHT -> DOWNLEFT
  }

fun Diagonal.rotateLeft() =
  when (this) {
    UPRIGHT -> UPLEFT
    UPLEFT -> DOWNLEFT
    DOWNLEFT -> DOWNRIGHT
    DOWNRIGHT -> UPRIGHT
  }

fun Diagonal.rotate90(clockwise: Boolean) = if (clockwise) rotateRight() else rotateLeft()

fun Direction.rotate45(clockwise: Boolean) =
  if (clockwise) {
    when (this) {
      UP -> UPRIGHT
      UPRIGHT -> RIGHT
      RIGHT -> DOWNRIGHT
      DOWNRIGHT -> DOWN
      DOWN -> DOWNLEFT
      DOWNLEFT -> LEFT
      LEFT -> UPLEFT
      UPLEFT -> UP
    }
  } else {
    when (this) {
      UP -> UPLEFT
      UPLEFT -> LEFT
      LEFT -> DOWNLEFT
      DOWNLEFT -> DOWN
      DOWN -> DOWNRIGHT
      DOWNRIGHT -> RIGHT
      RIGHT -> UPRIGHT
      UPRIGHT -> UP
    }
  }

enum class Axis {
  HORIZONTAL,
  VERTICAL,
}

fun Diagonal.component(orth: Orthogonal): Long {
  return when (orth) {
    UP -> offset.y * -1
    DOWN -> offset.y
    LEFT -> offset.x * -1
    RIGHT -> offset.x
  }
}

val Orthogonal.axis
  get() =
    when (this) {
      LEFT,
      RIGHT -> HORIZONTAL
      UP,
      DOWN -> VERTICAL
    }

fun Direction.toChar(): Char {
  return when (this) {
    DOWNLEFT -> TODO()
    DOWNRIGHT -> TODO()
    UPLEFT -> TODO()
    UPRIGHT -> TODO()
    DOWN -> '⇓'
    LEFT -> '⇐'
    RIGHT -> '⇒'
    UP -> '⇑'
  }
}
