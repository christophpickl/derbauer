package com.github.christophpickl.derbauer.upgrade

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class UpgradeTest {

    fun `Given not really enough gold When upgrade Then succeed because rounded price used`() {
        upgrade.buyPrice = Amount(1_999)
        Model.gold = Amount(1_990)

        doUpgrade(upgrade)

        assertThat(upgrade.currentLevel).isEqualTo(1)
        assertThat(Model.gold.real).isEqualTo(0)
    }

    fun `Given a bit more gold than needed When upgrade Then some gold left because rounded price is used`() {
        upgrade.buyPrice = Amount(1_010)
        Model.gold = Amount(1_017)

        doUpgrade(upgrade)

        assertThat(upgrade.currentLevel).isEqualTo(1)
        assertThat(Model.gold.real).isEqualTo(7)
    }

    private val upgrade get() = Model.player.upgrades.farmProductivity

    private fun doUpgrade(upgrade: Upgrade) {
        UpgradeController().doUpgrade(upgrade)
    }

}
