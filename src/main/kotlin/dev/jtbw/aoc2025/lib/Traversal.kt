package dev.jtbw.aoc2025.lib

fun <T> traversal(
  start: T,
  block: TraversalContext<T>.(T) -> Unit,
): Sequence<T> {
  return traversal(start, {it}, block)
}

fun <T, K> traversal(
  start: T,
  skipRepeatsBy: ((T) -> K)? = null,
  block: TraversalContext<T>.(T) -> Unit,
): Sequence<T> {
  val q = ArrayDeque<Pair<T, Boolean>>().apply { add(start to false) }
  val firstVisits = mutableListOf<T>()
  val lastVisits = mutableListOf<T>()
  val alreadyVisited = mutableSetOf<K>()

  return sequence {
    val context =
      object : TraversalContext<T> {
        override fun visitDFS(t: T) {
          firstVisits += t
        }

        override fun visitBFS(t: T) {
          lastVisits += t
        }
      }

    while (q.isNotEmpty()) {
      val (next, final) = q.removeFirst()

      if (skipRepeatsBy != null) {
        if (skipRepeatsBy(next) in alreadyVisited) {
          continue
        } else {
          alreadyVisited += skipRepeatsBy(next)
        }
      }

      firstVisits.clear()
      lastVisits.clear()
      context.block(next)
      yield(next)
      firstVisits.asReversed().forEach { q.addFirst(it to (it == next)) }
      lastVisits.forEach { q.addLast(it to (it == next)) }
    }
  }
}

fun <T> traversalWithPath(
  start: T,
  block: TraversalContext<T>.(pathInclusive: List<T>, node: T) -> Unit,
): Sequence<List<T>> {
  return traversal<List<T>, T>(
    start =listOf(start),
    skipRepeatsBy = { it.last() }
  ) { pathInclusive ->
    val originalContext = this
    val context = object : TraversalContext<T> {
      override fun visitDFS(t: T) {
        if(t == pathInclusive.last()) {
          originalContext.visitDFS(pathInclusive)
        } else {
          originalContext.visitDFS(pathInclusive + t)
        }
      }

      override fun visitBFS(t: T) {
        if(t == pathInclusive.last()) {
          originalContext.visitBFS(pathInclusive)
        } else {
          originalContext.visitBFS(pathInclusive + t)
        }
      }
    }
    context.block(pathInclusive, pathInclusive.last())
  }
}

interface TraversalContext<T> {
  fun visitDFS(t: T)

  fun visitBFS(t: T)
}

