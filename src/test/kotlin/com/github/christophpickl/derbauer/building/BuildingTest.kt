package com.github.christophpickl.derbauer.building

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.isAmountEqualTo
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class BuildingTest {

    private val building get() = Model.player.buildings.houses

    fun `Given enough land and gold When get effective buy possible Then return 1`() {
        Model.land = building.landNeeded
        Model.gold = building.buyPrice
        Model.player.buildings.all.forEach { it.amount = Amount.zero }

        assertThat(building.effectiveBuyPossibleAmount).isAmountEqualTo(1)
    }

    fun `Given enough land and lots of gold When get effective buy possible Then return 1`() {
        Model.land = building.landNeeded
        Model.gold = building.buyPrice * 42
        Model.player.buildings.all.forEach { it.amount = Amount.zero }

        assertThat(building.effectiveBuyPossibleAmount).isAmountEqualTo(1)
    }

    fun `Given lots of land and enough gold When get effective buy possible Then return 1`() {
        Model.land = building.landNeeded * 42
        Model.gold = building.buyPrice
        Model.player.buildings.all.forEach { it.amount = Amount.zero }

        assertThat(building.effectiveBuyPossibleAmount).isAmountEqualTo(1)
    }

    fun `Given more than enough land and enough gold and yet one existing When get effective buy possible Then return 1`() {
        Model.land = building.landNeeded * 2
        Model.gold = building.buyPrice * 2
        Model.player.buildings.all.forEach { it.amount = Amount.zero }
        building.amount = Amount.one

        assertThat(building.effectiveBuyPossibleAmount).isAmountEqualTo(1)
    }

}
