package com.github.christophpickl.derbauer.action.throneroom

interface ThroneRoomVisitor<C : ThroneRoomChoice> {
    val message: String
    val choices: List<C>
    fun condition(): Boolean
    /** Return null if choice was invalid. */
    fun choose(choice: C): String?
}
