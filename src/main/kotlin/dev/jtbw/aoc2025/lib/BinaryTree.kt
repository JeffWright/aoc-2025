package dev.jtbw.aoc2025.lib

data class BinaryTree<T>(
    var value: T,
    var left: BinaryTree<T>? = null,
    var right: BinaryTree<T>? = null,
)
