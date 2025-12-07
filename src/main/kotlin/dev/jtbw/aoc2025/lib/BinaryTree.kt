package dev.jtbw.aoc2025.lib

data class BinaryTree<T>(
  var value: T,
  var left: BinaryTree<T>? = null,
  var right: BinaryTree<T>? = null,
)

fun <T> BinaryTree<T>.traversePrefix(): Sequence<T> {
  val stack = ArrayDeque<BinaryTree<T>>()
  stack.addFirst(this)

  return sequence {
    do {
      val next = stack.removeFirst()
      yield(next.value)
      next.left?.let { stack.add(it) }
      next.right?.let { stack.add(it) }
    } while (stack.isNotEmpty())
  }
}
