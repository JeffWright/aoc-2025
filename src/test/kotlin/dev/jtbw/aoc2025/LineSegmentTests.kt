package dev.jtbw.aoc2025

import dev.jtbw.aoc2025.lib.twodeespace.LineSegment
import dev.jtbw.aoc2025.lib.twodeespace.Offset
import dev.jtbw.aoc2025.lib.twodeespace.Rect
import dev.jtbw.aoc2025.lib.twodeespace.intersects
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class LineSegmentTests :
  FunSpec({
    test("Line Segment Intercepts") {
      LineSegment(Offset(7, 3), Offset(7, 1)).intersects(Rect(7, 1, 11, 1)) shouldBe true

      LineSegment(Offset(0, 3), Offset(2, 3)).intersects(Rect(1, 1, 7, 7)) shouldBe true

      LineSegment(7, 0, 14, 2).intersects(LineSegment(7, 1, 7, 7)) shouldBe false

      LineSegment(Offset(7, 0), Offset(14, 2)).intersects(Rect(1, 1, 7, 7)) shouldBe false

      LineSegment(Offset(7, 3), Offset(5, 2)).intersects(Rect(1, 1, 5, 7)) shouldBe true
    }
  })
