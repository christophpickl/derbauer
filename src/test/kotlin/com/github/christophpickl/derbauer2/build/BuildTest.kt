package com.github.christophpickl.derbauer2.build

import com.github.christophpickl.derbauer2.TestModelListener
import com.github.christophpickl.derbauer2.hasSameAmountAs
import com.github.christophpickl.derbauer2.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class BuildTest {

    fun `Given enough gold and land When build Then succeed`() {
        Model.gold = building.buyPrice
        Model.land = building.landNeeded
        val before = building.amount

        build(building)

        building hasSameAmountAs before + 1
        assertThat(Model.gold).isEqualTo(0)
        assertThat(Model.land).isEqualTo(building.landNeeded)
        assertThat(Model.player.resources.land.usedAmount).isEqualTo(building.landNeeded)
    }

    fun `Given no gold When build Then fail`() {
        Model.gold = 0
        val before = building.amount

        build(building)

        building hasSameAmountAs before
    }

    fun `Given no land When build Then fail`() {
        Model.land = 0
        val before = building.amount

        build(building)

        building hasSameAmountAs before
    }

    fun `Given enough gold but not enough unused land When build Then fail`() {
        building.amount = 1
        Model.land = building.landNeeded
        Model.gold = building.buyPrice
        val before = building.amount

        build(building)

        building hasSameAmountAs before
    }

    private val building get() = Model.player.buildings.houses

    private fun build(building: Building) {
        BuildController().doBuild(BuildChoice(building))
    }

}
