package com.github.christophpickl.derbauer2.misc

import com.github.christophpickl.derbauer2.model.Amountable
import kotlin.reflect.KMutableProperty1

interface ReflectPlayer {
    fun playerRead(): Int
    fun playerChange(changeBy: Int)
}

class ReflectPlayerImpl<HOST, TARGET : Amountable>(
    private val host: HOST,
    private val playerProperty: KMutableProperty1<HOST, TARGET>
) : ReflectPlayer {

    override fun playerRead(): Int =
        playerProperty.get(host).amount

    override fun playerChange(changeBy: Int) {
        playerProperty.get(host).amount += changeBy
    }
}
