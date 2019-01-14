package com.github.christophpickl.derbauer.military.attack.target

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.data.ValuesAttackTarget
import com.github.christophpickl.derbauer.military.attack.AttackContext
import com.github.christophpickl.derbauer.military.attack.AttackLoot
import com.github.christophpickl.derbauer.model.Conditional
import com.github.christophpickl.derbauer.model.Entity
import com.github.christophpickl.derbauer.model.Labeled
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.Ordered
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.derbauer.model.filterConditional
import com.github.christophpickl.derbauer.model.ordered
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType
import java.util.*

interface AttackTarget : Entity, Labeled, Ordered, Conditional {
    val enemies: Amount
    fun enableNextTarget(): AttackTarget?
    fun computeLoot(context: AttackContext): AttackLoot
}

class Targets {

    val wildlings: WildlingsTarget = WildlingsTarget()
    val village: VillageTarget = VillageTarget()
    val town: TownTarget = TownTarget()
    val city: CityTarget = CityTarget()
    val empire: EmpireTarget = EmpireTarget()

    @get:JsonIgnore val all get() = propertiesOfType<Targets, AttackTarget>(this).ordered().filterConditional()
}

abstract class AbstractAttackTarget(
    override val label: String,
    private val values: ValuesAttackTarget
) : AttackTarget {
    companion object {
        private var counter = 0
        private val targetsByCounter = LinkedList<AbstractAttackTarget>()
    }

    init {
        @Suppress("LeakingThis")
        targetsByCounter += this
    }

    override val enemies = Amount(values.enemies)

    private var enabled = false

    final override val order = counter++

    override fun checkCondition() = if (order == 0) true else enabled

    override fun enableNextTarget(): AttackTarget? =
        if (order < counter - 1) {
            val nextTarget = targetsByCounter[order + 1]
            val wasEnabled = nextTarget.enabled
            nextTarget.enabled = true
            if (!wasEnabled) nextTarget else null
        } else null

    override fun computeLoot(context: AttackContext) = AttackLoot(
        goldEarned = values.loot.gold.compute(context.originalEnemies),
        foodEarned = Amount.minOf(values.loot.food.compute(context.originalEnemies), Model.foodCapacityLeft),
        landEarned = values.loot.land.compute(context.originalEnemies)
    )

    override fun toString() = label
}


class WildlingsTarget : AbstractAttackTarget(
    label = "Wildlings",
    values = Values.military.targets.wildlings
)

class VillageTarget : AbstractAttackTarget(
    label = "Village",
    values = Values.military.targets.village
)

class TownTarget : AbstractAttackTarget(
    label = "Town",
    values = Values.military.targets.town
)

class CityTarget : AbstractAttackTarget(
    label = "City",
    values = Values.military.targets.city
)

class EmpireTarget : AbstractAttackTarget(
    label = "Empire",
    values = Values.military.targets.empire
)

// country, nation, 
