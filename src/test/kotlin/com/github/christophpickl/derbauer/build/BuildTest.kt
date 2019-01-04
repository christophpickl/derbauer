package com.github.christophpickl.derbauer.build

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.hasSameAmountAs
import com.github.christophpickl.derbauer.isAmountEqualTo
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class BuildTest {

    fun `Given enough gold and land When build Then succeed`() {
        Model.gold = building.buyPrice
        Model.land = building.landNeeded

        build(building)

        building hasSameAmountAs 1
        assertThat(Model.gold).isAmountEqualTo(0)
        assertThat(Model.land).isEqualTo(building.landNeeded)
        assertThat(Model.player.resources.land.usedAmount).isEqualTo(building.landNeeded)
    }

    fun `Given no gold When build Then dont build`() {
        Model.gold = Amount.zero

        build(building)

        building hasSameAmountAs 0
    }

    fun `Given no land When build Then dont build`() {
        Model.land = Amount.zero

        build(building)

        building hasSameAmountAs 0
    }

    fun `Given enough gold but not enough unused land When build Then dont build`() {
        building.amount = Amount(1)
        Model.land = building.landNeeded
        Model.gold = building.buyPrice
        println(building.amount)

        build(building)

        building hasSameAmountAs 1
    }

    // FIXME check this for trade/upgrade as well
    fun `Given not really enough gold When build Then succeed because rounded price used`() {
        Model.land = building.landNeeded
        building.buyPrice = Amount(1_999)
        Model.gold = Amount(1_000)

        build(building)

        building hasSameAmountAs 1
        assertThat(Model.gold.real).isEqualTo(0)
    }

    fun `Given a bit more gold than needed When build Then some gold left because rounded price is used`() {
        Model.land = building.landNeeded
        building.buyPrice = Amount(1_000)
        Model.gold = Amount(1_042)

        build(building)

        assertThat(Model.gold.real).isEqualTo(42)
    }

    private val building get() = Model.player.buildings.houses

    private fun build(building: Building) {
        BuildController().doBuild(BuildChoice(building))
    }

}
