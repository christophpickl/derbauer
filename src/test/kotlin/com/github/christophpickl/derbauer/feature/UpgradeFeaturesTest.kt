package com.github.christophpickl.derbauer.feature

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class UpgradeFeaturesTest {

    fun `Given feature production upgrade satisfied Then feature is enabled`() {
        Model.food = Values.features.foodProductionUpgradeFoodNeeded
        Model.player.buildings.granaries.amount = Values.features.foodProductionUpgradeBuildingsNeeded

        assertThat(Model.features.upgrade.foodProductivityUpgrade).isEnabled()
    }

}
