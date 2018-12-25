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
    var soldiers: SoldierMilitary = SoldierMilitary(),
    var knights: KnightMilitary = KnightMilitary(),
    var catapults: CatapultMilitary = CatapultMilitary()
) {

    val all: List<Military> = propertiesOfType<Militaries, Military>(this).ordered()
    val totalAmount get() = all.sumBy { it.amount }
}

interface Military : Entity, Descriptable, MultiLabeled, Amountable, Buyable {
    var attackModifier: Double
}

interface PeopleMilitary : Military {
    var costsPeople: Int
    override val effectiveBuyPossibleAmount get() = Math.max(0, Math.min(buyPossibleAmount, (Model.people - 1) / costsPeople))
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
    protected abstract val preDescription: String
    final override fun description() = "$preDescription; " +
        "costs: $buyPrice gold${if (this is PeopleMilitary) " and $costsPeople people" else ""}; " +
        "ATT: $attackModifier"

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
    override val preDescription = "basic unit"
}

class KnightMilitary : AbstractMilitary(
    labelSingular = "knight",
    labelPlural = "knights",
    amount = VALUES.knights,
    buyPrice = 30,
    attackModifier = 1.2
), PeopleMilitary {
    override var costsPeople = 1
    override val preDescription = "allrounder unit"
}

class CatapultMilitary : AbstractMilitary(
    labelSingular = "catapult",
    labelPlural = "catapults",
    amount = VALUES.catapults,
    buyPrice = 50,
    attackModifier = 1.4
), PeopleMilitary {
    override var costsPeople = 3
    override val preDescription = "good against buildings"
}
