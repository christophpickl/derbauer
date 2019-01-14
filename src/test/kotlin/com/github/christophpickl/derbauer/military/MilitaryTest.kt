package com.github.christophpickl.derbauer.military

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.building.ArmyCapacityBuilding
import com.github.christophpickl.derbauer.hasSameAmountAs
import com.github.christophpickl.derbauer.isAmountEqualTo
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

abstract class BaseMilitaryTest {

    protected val unit get() = Model.player.armies.soldiers

    protected fun hire(toHire: Army, amount: Long) {
        MilitaryController().doHire(toHire, amount)
    }

    protected fun ensureEnoughGoldPeopleCapacityAndUpgrade(army: Army) {
        Model.gold = unit.buyPrice
        Model.people = army.costsPeople + 1
        Model.player.buildings.barracks.amount = Amount(1)
        Model.player.upgrades.militaryUpgrade.setToMaxLevel()
        Model.features.checkAndNotifyAll()
    }
}

@Test
@Listeners(TestModelListener::class)
class MilitaryTest : BaseMilitaryTest() {

    fun `Given all good but no military cap When hire Then not possible and dont hire`() {
        Model.gold = unit.buyPrice
        Model.people = unit.costsPeople
        Model.player.buildings.all.filterIsInstance<ArmyCapacityBuilding>().forEach { it.amount.isZero }

        assertThat(unit.effectiveBuyPossibleAmount).isAmountEqualTo(0)
        hire(unit, 1)

        unit hasSameAmountAs 0
    }

    fun `Given all good When hire Then possible and hire`() {
        ensureEnoughGoldPeopleCapacityAndUpgrade(unit)

        hire(unit, 1)

        unit hasSameAmountAs 1
    }

    fun `Given all good Then effective buy possible is 1`() {
        ensureEnoughGoldPeopleCapacityAndUpgrade(unit)

        assertThat(unit.effectiveBuyPossibleAmount).isAmountEqualTo(1)
    }

    fun `Given not really enough gold When hire Then succeed because rounded price used`() {
        ensureEnoughGoldPeopleCapacityAndUpgrade(unit)
        unit.buyPrice = Amount(1_999)
        Model.gold = Amount(1_990)

        hire(unit, 1)

        unit hasSameAmountAs 1
        assertThat(Model.gold.real).isEqualTo(0)
    }

    fun `Given a bit more gold than needed When build Then some gold left because rounded price is used`() {
        ensureEnoughGoldPeopleCapacityAndUpgrade(unit)
        unit.buyPrice = Amount(1_010)
        Model.gold = Amount(1_017)

        hire(unit, 1)

        assertThat(Model.gold.real).isEqualTo(7)
    }

    fun `Given not really enough people When hire Then succeed because rounded people used`() {
        ensureEnoughGoldPeopleCapacityAndUpgrade(unit)
        unit.costsPeople = Amount(1_999)
        Model.people = Amount(1_990)

        hire(unit, 1)

        unit hasSameAmountAs 1
        assertThat(Model.people.real).isEqualTo(0)
    }

    fun `Given a bit more people than needed When build Then some people left because rounded people is used`() {
        ensureEnoughGoldPeopleCapacityAndUpgrade(unit)
        unit.costsPeople = Amount(1_010)
        Model.people = Amount(1_017)

        hire(unit, 1)

        assertThat(Model.people.real).isEqualTo(7)
    }

}
