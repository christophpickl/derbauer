package com.github.christophpickl.derbauer.military

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.buysell.Buyable
import com.github.christophpickl.derbauer.data.ValueArmy
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Amountable
import com.github.christophpickl.derbauer.model.Conditional
import com.github.christophpickl.derbauer.model.Describable
import com.github.christophpickl.derbauer.model.Entity
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.MultiLabeled
import com.github.christophpickl.derbauer.model.Ordered
import com.github.christophpickl.derbauer.model.filterConditional
import com.github.christophpickl.derbauer.model.ordered
import com.github.christophpickl.derbauer.model.sumBy
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType
import com.github.christophpickl.kpotpourri.common.string.IgnoreStringified
import com.github.christophpickl.kpotpourri.common.string.Stringifier

data class Armies(
    var soldiers: Soldier = Soldier(),
    var knights: Knight = Knight(),
    var catapults: Catapult = Catapult()
) {

    @get:JsonIgnore val all get() = propertiesOfType<Armies, Army>(this).ordered().filterConditional()
    inline fun <reified T : Army> filterAll() = all.filterIsInstance<T>()
    
    val totalAmount get() = all.sumBy { it.amount }
    val armyCapacityLeft get() = Model.player.buildings.totalArmyCapacity - totalAmount
}

interface Army : Entity, Describable, MultiLabeled, Amountable, Buyable, Ordered {
    var attackModifier: Double
    var costsPeople: Amount

    override val effectiveBuyPossibleAmount
        get() = Amount.minOfNonNegative(
            buyPossibleAmount,
            (Model.people - Amount.one) / costsPeople,
            Model.armyCapacityLeft
        )
}

abstract class AbstractArmy(
    override val labelSingular: String,
    override val labelPlural: String,
    value: ValueArmy
) : Army {

    final override var amount = value.amount
    final override var buyPrice = value.buyPrice
    final override var attackModifier = value.attackModifier
    final override var costsPeople = value.costsPeople
    final override val buyDescription get() = "${buyPrice.formatted} gold and ${costsPeople.formatted} people"

    companion object {
        private var counter = 0
    }

    @IgnoreStringified final override val order = counter++
    override fun toString() = Stringifier.stringify(this)
}

class Soldier : AbstractArmy(
    labelSingular = "soldier",
    labelPlural = "soldiers",
    value = Values.militaries.soldiers
) {
    override val description get() = "basic unit; attack: $attackModifier"
}

class Knight : AbstractArmy(
    labelSingular = "knight",
    labelPlural = "knights",
    value = Values.militaries.knights
), Conditional {
    override fun checkCondition() = Model.features.military.knights.isEnabled()
    override val description get() = "allrounder unit; attack: $attackModifier"
}

class Catapult : AbstractArmy(
    labelSingular = "catapult",
    labelPlural = "catapults",
    value = Values.militaries.catapults
), Conditional {
    override fun checkCondition() = Model.features.military.catapults.isEnabled()
    override val description get() = "good against buildings; attack: $attackModifier"
}
