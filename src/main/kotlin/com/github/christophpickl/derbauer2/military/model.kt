package com.github.christophpickl.derbauer2.military

import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Amountable
import com.github.christophpickl.derbauer2.model.Descriptable
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.MultiLabeled
import com.github.christophpickl.derbauer2.model.Ordered
import com.github.christophpickl.derbauer2.model.ordered
import com.github.christophpickl.derbauer2.trade.Buyable

interface Military : MultiLabeled, Amountable, Ordered, Buyable, Descriptable {
    var attackModifier: Double
    var costsPeople: Int
}

data class PlayerMilitary(
    var soldiers: SoldierMilitary = SoldierMilitary()
) {

    val all: List<Military> = propertiesOfType<PlayerMilitary, Military>(this).ordered()
    val totalCount = all.sumBy { it.amount }

    fun killRandom() {
        all.first { it.amount > 0 }.amount--
    }
}

class SoldierMilitary : Military {
    override val labelSingular = "soldier"
    override val labelPlural = "soldiers"
    override var amount = 0
    override val order = 0
    override var buyPrice = 20
    override val description = "Basic unit"

    override var attackModifier = 1.0
    override var costsPeople = 1

    override val effectiveBuyPossibleAmount get() = Math.min(buyPossibleAmount, Model.people)

    override fun toString() = Stringifier.stringify(this)
}
