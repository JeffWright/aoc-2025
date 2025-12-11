package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.lib.BinaryTree
import dev.jtbw.aoc2025.lib.traversal
import dev.jtbw.aoc2025.lib.traversalWithPath
import dev.jtbw.aoc2025.lib.twodeespace.Direction
import dev.jtbw.aoc2025.lib.twodeespace.Offset
import dev.jtbw.aoc2025.lib.twodeespace.Rect
import dev.jtbw.aoc2025.lib.twodeespace.contains
import dev.jtbw.aoc2025.lib.twodeespace.plus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TraversalTests :
  FunSpec({
    val root = BinaryTree<Int>(4)
    root.left = BinaryTree(2)
    root.left!!.left = BinaryTree(1)
    root.left!!.right = BinaryTree(3)

    root.right = BinaryTree(6)
    root.right!!.left = BinaryTree(5)

    test("Walk a BinaryTree DFS") {
      var numChecks = 0
      traversal(root) { node ->
        numChecks++
        node.left?.let { visitDFS(it) }
        node.right?.let { visitDFS(it) }
      }
        .map { it.value }
        .toList() shouldBe(listOf(4, 2, 1, 3, 6, 5))

      numChecks shouldBe 6
    }

    test("Walk a BinaryTree BFS") {
      traversal(root) { node ->
        node.left?.let { visitBFS(it) }
        node.right?.let { visitBFS(it) }
      }
        .map { it.value }
        .toList() shouldBe(listOf(4, 2, 6, 1, 3, 5))
    }

    test("Stop early") {
      var numChecks = 0
      traversal(root) { node ->
        numChecks++
        node.left?.let { visitDFS(it) }
        node.right?.let { visitDFS(it) }
      }
        .first { it.value == 2 }

      numChecks shouldBe 2
    }

    test ("Walk a Rect's offsets") {

      val rect = Rect(Offset(0, 0), Offset(3, 3))

      var numChecks = 0
      traversal(Offset(0, 0)) { node ->
        numChecks++
        d("T: $node")
        Direction.orthogonals.forEach {
          val next = node + it
          if (next in rect) {
            visitBFS(next)
          }
        }
      }
        .toList().size shouldBe 16
      numChecks shouldBe 16
    }

    test("Find a path in a BinaryTree - BFS") {
      var checks = mutableListOf<Int>()
      traversalWithPath(root) { path, node->
        val debugPath = path.map { it.value }.joinToString()
        checks += node.value
        node.left?.let { visitBFS(it) }
        node.right?.let { visitBFS(it) }
      }
        .first { path ->
          path.last().value == 3
        }
        .map { it.value }
        .shouldBe(listOf(4, 2, 3))

      checks shouldBe listOf(4, 2, 6, 1, 3)
    }

    test("Find a path in a BinaryTree - DFS") {
      var checks = mutableListOf<Int>()
      traversalWithPath(root) { path, node->
        val debugPath = path.map { it.value }.joinToString()
        checks += node.value
        node.left?.let { visitDFS(it) }
        node.right?.let { visitDFS(it) }
      }
        .first { path ->
          path.last().value == 3
        }
        .map { it.value }
        .shouldBe(listOf(4, 2, 3))

      checks shouldBe listOf(4, 2, 1, 3)
    }

    test("Find a path through a Rect - BFS") {
      val rect = Rect(Offset(0, 0), Offset(3, 3))

      var checks = mutableListOf<Offset>()
      val path = traversalWithPath(Offset(0, 0)) { path, node->
        checks+= node
        Direction.orthogonals.forEach {
          val next = node + it
          if (next in rect) {
            visitBFS(next)
          }
        }
      }
        .first { path ->
          path.last() == Offset(3, 3)
        }

      path.size shouldBe 7
    }

    test("Find a path through a Rect - DFS") {
      val rect = Rect(Offset(0, 0), Offset(3, 3))

      var checks = mutableListOf<Offset>()
      val path = traversalWithPath(Offset(0, 0)) { path, node->
        checks+= node
        Direction.orthogonals.forEach {
          val next = node + it
          if (next in rect) {
            visitDFS(next)
          }
        }
      }
        .first { path ->
          path.last() == Offset(3, 3)
        }

      path.size shouldBe 7
      checks.size shouldBe 7
    }
})
