package com.github.christophpickl.derbauer2.military

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.data.ValueMilitary
import com.github.christophpickl.derbauer2.data.Values
import com.github.christophpickl.derbauer2.model.Amountable
import com.github.christophpickl.derbauer2.model.Conditional
import com.github.christophpickl.derbauer2.model.Describable
import com.github.christophpickl.derbauer2.model.Entity
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.MultiLabeled
import com.github.christophpickl.derbauer2.model.Ordered
import com.github.christophpickl.derbauer2.model.filterConditional
import com.github.christophpickl.derbauer2.model.ordered
import com.github.christophpickl.derbauer2.trade.Buyable
import com.github.christophpickl.kpotpourri.common.math.KMath
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType
import com.github.christophpickl.kpotpourri.common.string.IgnoreStringified
import com.github.christophpickl.kpotpourri.common.string.Stringifier

data class Militaries(
    var soldiers: SoldierMilitary = SoldierMilitary(),
    var knights: KnightMilitary = KnightMilitary(),
    var catapults: CatapultMilitary = CatapultMilitary()
) {

    @get:JsonIgnore val all get() = propertiesOfType<Militaries, Military>(this).ordered().filterConditional()
    inline fun <reified T : Military> filterAll() = all.filterIsInstance<T>()
    
    val totalAmount get() = all.sumBy { it.amount }
    val militaryCapacityLeft get() = Model.player.buildings.totalMilitaryCapacity - totalAmount
}

interface Military : Entity, Describable, MultiLabeled, Amountable, Buyable, Ordered {
    var attackModifier: Double
    var costsPeople: Int

    override val effectiveBuyPossibleAmount
        get() = KMath.minButNotNegative(
            buyPossibleAmount,
            (Model.people - 1) / costsPeople,
            Model.militaryCapacityLeft
        )
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
    final override val buyDescription get() = "$buyPrice gold and $costsPeople people"

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
    override val description get() = "basic unit; attack: $attackModifier"
}

class KnightMilitary : AbstractMilitary(
    labelSingular = "knight",
    labelPlural = "knights",
    value = Values.militaries.knights
), Conditional {
    override fun checkCondition() = Model.features.military.knights.isEnabled()
    override val description get() = "allrounder unit; attack: $attackModifier"
}

class CatapultMilitary : AbstractMilitary(
    labelSingular = "catapult",
    labelPlural = "catapults",
    value = Values.militaries.catapults
), Conditional {
    override fun checkCondition() = Model.features.military.catapults.isEnabled()
    override val description get() = "good against buildings; attack: $attackModifier"
}
