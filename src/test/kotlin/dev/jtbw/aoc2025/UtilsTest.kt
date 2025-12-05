package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.lib.IndexedValue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class UtilsTest :
  FunSpec({
    test("Long.repeat") {
      // TODO: Add test cases for repeat function
      // Example: 123.repeat(2) should equal 123123
      val result = 0L

      result shouldBe 0L
    }

    test("Sequence.distinctUntilChanged") {
      // TODO: Add test cases for distinctUntilChanged
      // Example: sequenceOf(1, 1, 2, 2, 3).distinctUntilChanged().toList() should equal [1, 2, 3]
      val result = emptyList<Int>()

      result shouldBe emptyList()
    }

    test("Iterator.nextOrNull") {
      // TODO: Add test cases for nextOrNull
      // Example: iterator with elements should return next element, empty iterator should return
      // null
      val result: Int? = null

      result shouldBe null
    }

    test("Iterable.multiMaxBy") {
      // TODO: Add test cases for multiMaxBy
      // Example: listOf(1, 5, 3, 9, 2).multiMaxBy(3) { it } should return top 3 values
      val result = emptyList<IndexedValue<Int>>()

      result shouldBe emptyList()
    }
  })
