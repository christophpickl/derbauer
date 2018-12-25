package com.github.christophpickl.derbauer2.feature

import com.github.christophpickl.derbauer2.TestModelListener
import com.github.christophpickl.derbauer2.Values
import com.github.christophpickl.derbauer2.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test


@Test
@Listeners(TestModelListener::class)
class UpgradeFeaturesTest {

    fun `Given feature production upgrade satisfied Then feature is enabled`() {
        Model.food = Values.features.foodProductionUpgradeFoodNeeded
        Model.player.buildings.granaries.amount = Values.features.foodProductionUpgradeBuildingsNeeded

        assertThat(Model.features.upgrade.isFoodProductionUpgradeEnabled).isTrue()
    }

}
