package com.github.christophpickl.derbauer.military

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.build.MilitaryCapacityBuilding
import com.github.christophpickl.derbauer.hasSameAmountAs
import com.github.christophpickl.derbauer.isAmountEqualTo
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.Renderer
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class MilitaryTest {

    private val renderer = mock<Renderer>()

    fun `Given all good but no military cap When hire Then not possible and dont hire`() {
        Model.gold = unit.buyPrice
        Model.people = unit.costsPeople
        Model.player.buildings.all.filterIsInstance<MilitaryCapacityBuilding>().forEach { it.amount.isZero }

        assertThat(unit.effectiveBuyPossibleAmount).isAmountEqualTo(0)
        hire(unit, 1)

        unit hasSameAmountAs 0
    }


    fun `Given all good When hire Then possible and hire`() {
        ensureEnoughGoldPeopleCapacityAndUpgrade(unit)
        assertThat(unit.effectiveBuyPossibleAmount).isAmountEqualTo(1)

        hire(unit, 1)

        unit hasSameAmountAs 1
    }

    fun `Given not really enough gold When hire Then succeed because rounded price used`() {
        ensureEnoughGoldPeopleCapacityAndUpgrade(unit)
        unit.buyPrice = Amount(1_999)
        Model.gold = Amount(1_000)

        hire(unit, 1)

        unit hasSameAmountAs 1
        assertThat(Model.gold.real).isEqualTo(0)
    }

    fun `Given a bit more gold than needed When build Then some gold left because rounded price is used`() {
        ensureEnoughGoldPeopleCapacityAndUpgrade(unit)
        unit.buyPrice = Amount(1_000)
        Model.gold = Amount(1_042)

        hire(unit, 1)

        assertThat(Model.gold.real).isEqualTo(42)
    }

    fun `Given not really enough people When hire Then succeed because rounded people used`() {
        ensureEnoughGoldPeopleCapacityAndUpgrade(unit)
        unit.costsPeople = Amount(1_999)
        Model.people = Amount(1_000)

        hire(unit, 1)

        unit hasSameAmountAs 1
        assertThat(Model.people.real).isEqualTo(0)
    }

    fun `Given a bit more people than needed When build Then some people left because rounded people is used`() {
        ensureEnoughGoldPeopleCapacityAndUpgrade(unit)
        unit.costsPeople = Amount(1_000)
        Model.people = Amount(1_042)

        hire(unit, 1)

        assertThat(Model.people.real).isEqualTo(42)
    }

    private val unit get() = Model.player.militaries.soldiers

    private fun hire(toHire: Military, amount: Long) {
        MilitaryController(renderer).doHire(toHire, amount)
    }

    private fun ensureEnoughGoldPeopleCapacityAndUpgrade(military: Military) {
        Model.gold = unit.buyPrice
        Model.people = military.costsPeople + 1
        Model.player.buildings.barracks.amount = Amount(1)
        Model.player.upgrades.militaryUpgrade.setToMaxLevel()
    }

}
