package com.github.christophpickl.derbauer2.military

import com.github.christophpickl.derbauer2.VALUES
import com.github.christophpickl.derbauer2.misc.IgnoreStringified
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Amountable
import com.github.christophpickl.derbauer2.model.Descriptable
import com.github.christophpickl.derbauer2.model.Entity
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.MultiLabeled
import com.github.christophpickl.derbauer2.model.ordered
import com.github.christophpickl.derbauer2.trade.Buyable

data class Militaries(
    var soldiers: SoldierMilitary = SoldierMilitary()
) {

    val all: List<Military> = propertiesOfType<Militaries, Military>(this).ordered()
    val totalCount get() = all.sumBy { it.amount }

    fun killRandom() {
        all.first { it.amount > 0 }.amount--
    }
}

interface Military : Entity, Descriptable, MultiLabeled, Amountable, Buyable {
    var attackModifier: Double
}

interface PeopleMilitary : Military {
    var costsPeople: Int
}

abstract class AbstractMilitary(
    override val labelSingular: String,
    override val labelPlural: String,
    override var amount: Int,
    override var buyPrice: Int,
    override var attackModifier: Double
) : Military {
    companion object {
        private var counter = 0
    }

    @IgnoreStringified
    override val order = counter++

    override fun toString() = Stringifier.stringify(this)
}

class SoldierMilitary : AbstractMilitary(
    labelSingular = "soldier",
    labelPlural = "soldiers",
    amount = VALUES.soldiers,
    buyPrice = 20,
    attackModifier = 1.0
), PeopleMilitary {
    override var costsPeople = 1
    override val effectiveBuyPossibleAmount get() = Math.min(buyPossibleAmount, Model.people - 1)
    override fun description() = "basic unit; costs: $buyPrice gold and $costsPeople people; ATT: $attackModifier"
}
