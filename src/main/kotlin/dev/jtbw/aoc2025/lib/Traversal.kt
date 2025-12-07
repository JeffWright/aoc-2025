package dev.jtbw.aoc2025.lib

import dev.jtbw.aoc2025.d
import dev.jtbw.aoc2025.lib.twodeespace.Direction
import dev.jtbw.aoc2025.lib.twodeespace.Offset
import dev.jtbw.aoc2025.lib.twodeespace.Rect
import dev.jtbw.aoc2025.lib.twodeespace.contains
import dev.jtbw.aoc2025.lib.twodeespace.plus

fun <T> traversal(
  start: T,
  skipRepeats: Boolean = false,
  block: TraversalContext<T>.(T) -> Unit,
): Sequence<T> {
  val q = ArrayDeque<Pair<T, Boolean>>().apply { add(start to false) }
  val firstVisits = mutableListOf<T>()
  val lastVisits = mutableListOf<T>()
  val alreadyVisited = mutableSetOf<T>()

  return sequence {
    val context =
      object : TraversalContext<T> {
        override fun visit(t: T) {
          firstVisits += t
        }

        override fun visitLast(t: T) {
          lastVisits += t
        }
      }

    while (q.isNotEmpty()) {
      val (next, final) = q.removeFirst()
      if (final) {
        yield(next)
        continue
      }

      if (skipRepeats && next in alreadyVisited) {
        continue
      } else {
        alreadyVisited += next
      }

      firstVisits.clear()
      lastVisits.clear()
      context.block(next)
      firstVisits.asReversed().forEach { q.addFirst(it to (it == next)) }
      lastVisits.asReversed().forEach { q.addLast(it to (it == next)) }
    }
  }
}

interface TraversalContext<T> {
  fun visit(t: T)

  fun visitLast(t: T)
}

fun test() {
  val root = BinaryTree<Int>(4)
  root.left = BinaryTree(2)
  root.left!!.left = BinaryTree(1)
  root.left!!.right = BinaryTree(3)

  root.right = BinaryTree(6)
  root.right!!.left = BinaryTree(5)

  d("Infix")
  traversal(root) { node ->
      node.left?.let { visit(it) }
      visit(node)
      node.right?.let { visit(it) }
    }
    .map { it.value }
    .toList()
    .let { println(it) }

  val rect = Rect(Offset(0, 0), Offset(3, 3))

  traversal(Offset(0, 0), skipRepeats = true) { node ->
      d("T: $node")
      visit(node)
      Direction.orthogonals.forEach {
        val next = node + it
        if (next in rect) {
          visitLast(next)
        }
      }
    }
    .toList()
    .let { println(it) }
}

fun main() {
  test()
}
