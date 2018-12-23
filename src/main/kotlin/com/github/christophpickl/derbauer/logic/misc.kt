package com.github.christophpickl.derbauer.logic

val VIEW_SIZE = Size(width = 100, height = 30)

data class Size(
    val width: Int,
    val height: Int
)

data class Pos(
    val x: Int,
    val y: Int
) {
    init {
        require(x >= 0 && x < (VIEW_SIZE.width - 1))
        require(y >= 0 && y < (VIEW_SIZE.height - 1))
    }
}

fun beep() {
    println("beep")// TODO
}

fun <T> beepReturn(): T? {
    beep()
    return null
}
