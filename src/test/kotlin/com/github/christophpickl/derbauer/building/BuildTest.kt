package com.github.christophpickl.derbauer.building

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.hasSameAmountAs
import com.github.christophpickl.derbauer.isAmountEqualTo
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class BuildTest {

    fun `Given enough gold and land When build Then succeed`() {
        Model.player.buildings.allAmountToZero()
        Model.gold = building.buyPrice
        Model.land = building.landNeeded

        val wasBuilt = build(building)

        assertThat(wasBuilt).isEqualTo(true)
        building hasSameAmountAs 1
        assertThat(Model.gold).isAmountEqualTo(0)
        assertThat(Model.player.resources.land.unusedAmount).isEqualTo(Amount.zero)
        assertThat(Model.player.resources.land.usedAmount).isEqualTo(Model.land)
    }

    fun `Given no gold When build Then dont build`() {
        Model.gold = Amount.zero

        val wasBuilt = build(building)

        assertThat(wasBuilt).isEqualTo(false)
    }

    fun `Given no land When build Then dont build`() {
        Model.land = Amount.zero

        val wasBuilt = build(building)

        assertThat(wasBuilt).isEqualTo(false)
    }

    fun `Given enough gold but not enough unused land When build Then dont build`() {
        Model.player.buildings.allAmountToZero()
        building.amount = Amount(1)
        Model.land = building.landNeeded
        Model.gold = building.buyPrice

        val wasBuilt = build(building)

        assertThat(wasBuilt).isEqualTo(false)
    }

    fun `Given not really enough gold When build Then succeed because rounded price used`() {
        Model.player.buildings.allAmountToZero()
        Model.land = building.landNeeded
        building.buyPrice = Amount(1_999)
        Model.gold = Amount(1_990)

        val wasBuilt = build(building)

        assertThat(wasBuilt).isEqualTo(true)
        building hasSameAmountAs 1
        assertThat(Model.gold.real).isEqualTo(0)
    }

    fun `Given a bit more gold than needed When build Then succeed and some gold left because rounded price is used`() {
        Model.land += building.landNeeded
        building.buyPrice = Amount(1_010)
        Model.gold = Amount(1_017)

        val wasBuilt = build(building)

        assertThat(wasBuilt).isEqualTo(true)
        assertThat(Model.gold.real).isEqualTo(7)
    }

    private val building get() = Model.player.buildings.houses

    private fun build(building: Building) =
        BuildController().doBuild(BuildChoice(building))

}
