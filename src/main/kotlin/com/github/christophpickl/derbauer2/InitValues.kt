package com.github.christophpickl.derbauer2

val INIT_VALUES = if (CHEAT_MODE) CheatValues else DefaultInitValues

interface InitValues {
    val gold: Int
    val food: Int
    val people: Int
    val land: Int

    val houses: Int
    val granaries: Int
    val farms: Int
    val castles: Int
}

object DefaultInitValues : InitValues {
    override val gold = 100
    override val food = 300
    override val people = 2
    override val land = 5

    override val houses = 1
    override val granaries = 1
    override val farms = 1
    override val castles = 0
}

object CheatValues : InitValues {
    override val gold = 900
    override val food = 400
    override val people = 10
    override val land = 100

    override val houses = 10
    override val granaries = 5
    override val farms = 5
    override val castles = 0
}
