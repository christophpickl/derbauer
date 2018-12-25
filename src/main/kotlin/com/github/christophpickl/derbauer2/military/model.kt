package com.github.christophpickl.derbauer2.military

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.ValueMilitary
import com.github.christophpickl.derbauer2.Values
import com.github.christophpickl.derbauer2.misc.IgnoreStringified
import com.github.christophpickl.derbauer2.misc.Stringifier
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Amountable
import com.github.christophpickl.derbauer2.model.Descriptable
import com.github.christophpickl.derbauer2.model.Entity
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.MultiLabeled
import com.github.christophpickl.derbauer2.model.filterConditional
import com.github.christophpickl.derbauer2.model.ordered
import com.github.christophpickl.derbauer2.trade.Buyable

data class Militaries(
    var soldiers: SoldierMilitary = SoldierMilitary(),
    var knights: KnightMilitary = KnightMilitary(),
    var catapults: CatapultMilitary = CatapultMilitary()
) {

    @get:JsonIgnore val all get() = propertiesOfType<Militaries, Military>(this).ordered().filterConditional()
    val totalAmount get() = all.sumBy { it.amount }
}

interface Military : Entity, Descriptable, MultiLabeled, Amountable, Buyable {
    var attackModifier: Double
    var costsPeople: Int
    override val effectiveBuyPossibleAmount get() = Math.max(0, Math.min(buyPossibleAmount, (Model.people - 1) / costsPeople))
}


abstract class AbstractMilitary(
    override val labelSingular: String,
    override val labelPlural: String,
    value: ValueMilitary
) : Military {

    final override var amount = value.amount
    final override var buyPrice = value.buyPrice
    final override var attackModifier = value.attackModifier
    final override var costsPeople = value.costsPeople
    final override fun buyDescription() = "$buyPrice gold and $costsPeople people"

    companion object {
        private var counter = 0
    }

    @IgnoreStringified final override val order = counter++
    override fun toString() = Stringifier.stringify(this)
}

class SoldierMilitary : AbstractMilitary(
    labelSingular = "soldier",
    labelPlural = "soldiers",
    value = Values.militaries.soldiers
) {
    override fun description() = "basic unit; attack: $attackModifier"
}

class KnightMilitary : AbstractMilitary(
    labelSingular = "knight",
    labelPlural = "knights",
    value = Values.militaries.knights
) {
    override fun description() = "allrounder unit; attack: $attackModifier"
}

class CatapultMilitary : AbstractMilitary(
    labelSingular = "catapult",
    labelPlural = "catapults",
    value = Values.militaries.catapults
) {
    override fun description() = "good against buildings; attack: $attackModifier"
}
