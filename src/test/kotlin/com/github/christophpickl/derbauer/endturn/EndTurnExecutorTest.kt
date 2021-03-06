package com.github.christophpickl.derbauer.endturn

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.kpotpourri.common.random.RandomServiceAlwaysMinimum
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class EndTurnExecutorTest {

    // KARMA ===========================================================================================================
    
    fun `Given positive karma When end turn Then karma balanced to zero`() {
        Model.global.karma = 0.1

        execute()

        assertThat(Model.global.karma).isEqualTo(0.1 - Values.karma.karmaTurnBalancer)
    }

    fun `Given negative karma When end turn Then karma balanced to zero`() {
        Model.global.karma = -0.1

        execute()

        assertThat(Model.global.karma).isEqualTo(-0.1 + Values.karma.karmaTurnBalancer)
    }

    fun `Given zero karma When end turn Then karma stays zero`() {
        Model.global.karma = 0.0

        execute()

        assertThat(Model.global.karma).isEqualTo(0.0)
    }

    fun `Given positive karma close to zero When end turn Then karma gets zero`() {
        Model.global.karma = 0.001

        execute()

        assertThat(Model.global.karma).isEqualTo(0.0)
    }

    fun `Given negative karma close to zero When end turn Then karma gets zero`() {
        Model.global.karma = -0.001

        execute()

        assertThat(Model.global.karma).isEqualTo(0.0)
    }

    // ARMY LOST =======================================================================================================

    fun `Given zero gold and some soldiers When end turn Then armies lost`() {
        Model.gold = Amount.zero
        val originalArmies = 10L
        Model.player.armies.soldiers.amount = Amount(originalArmies)
        Model.player.buildings.houses.amount = Amount.one
        Model.food = Amount(20)
        
        execute()

        assertThat(Model.player.armies.soldiers.amount.real).isEqualTo(originalArmies - 1)
    }

    // TEST INFRA ======================================================================================================
    
    private fun execute() = EndTurnExecutor(RandomServiceAlwaysMinimum).execute()

}
