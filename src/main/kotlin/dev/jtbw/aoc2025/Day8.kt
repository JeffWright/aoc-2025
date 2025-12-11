package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.lib.pairwise
import dev.jtbw.aoc2025.lib.splitCommas
import dev.jtbw.aoc2025.lib.toLongs
import dev.jtbw.aoc2025.lib.twodeespace.Offset3
import dev.jtbw.aoc2025.lib.twodeespace.fastDistanceTo
import java.util.PriorityQueue

object Day8 {

  fun part1(input: String, numConnections: Int = 1_000): Any {
    val points =
      input.trim().lines().map {
        val (x, y, z) = it.splitCommas().toLongs()
        Offset3(x, y, z)
      }

    val clusters = mutableListOf<MutableSet<Offset3>>()

    val pq = PriorityQueue(compareBy<Pair<Offset3, Offset3>> { (a, b) -> a.fastDistanceTo(b) })
    pq.addAll(points.pairwise())

    repeat(numConnections) {
      val (a, b) = pq.remove()

      val clusterA = clusters.firstOrNull { a in it }
      val clusterB = clusters.firstOrNull { b in it }

      if (clusterA != null && clusterA == clusterB) {
        return@repeat
      } else if (clusterA == null && clusterB == null) {
        val newCluster = mutableSetOf(a, b)
        clusters += newCluster
      } else if (clusterA != null) {
        clusterA.addAll(clusterB ?: setOf(b))
        clusters.remove(clusterB)
      } else if (clusterB != null) {
        clusterB.add(a)
      }
    }

    return clusters.map { it.size }.sortedDescending().take(3).reduce(Int::times)
  }

  fun part2(input: String): Any {
    val points =
      input.trim().lines().map {
        val (x, y, z) = it.splitCommas().toLongs()
        Offset3(x, y, z)
      }

    val clusters = mutableListOf<MutableSet<Offset3>>()

    val pq = PriorityQueue(compareBy<Pair<Offset3, Offset3>> { (a, b) -> a.fastDistanceTo(b) })
    pq.addAll(points.pairwise())

    while (true) {
      val (a, b) = pq.remove()

      val clusterA = clusters.firstOrNull { a in it }
      val clusterB = clusters.firstOrNull { b in it }

      if (clusterA != null && clusterA == clusterB) {
        // nothing
      } else if (clusterA == null && clusterB == null) {
        val newCluster = mutableSetOf(a, b)
        clusters += newCluster
      } else if (clusterA != null) {
        clusterA.addAll(clusterB ?: setOf(b))
        clusters.remove(clusterB)
      } else if (clusterB != null) {
        clusterB.add(a)
      }

      if (clusters.size == 1 && clusters.first().size == points.size) {
        return a.x * b.x
      }
    }
  }
}
