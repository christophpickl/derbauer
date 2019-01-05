package com.github.christophpickl.derbauer.endturn

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.building.foodProducerAmountToZero
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.isAmountEqualTo
import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.random.RandomServiceAlwaysMinimum
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class EndTurnExecutorTest {

    fun `Given small food big people When end turn Then food is empty`() {
        Model.food = Amount(1)
        Model.people = Amount(2)
        Model.player.buildings.houses.amount = Amount(1)
        Model.player.buildings.foodProducerAmountToZero()

        val report = execute()

        assertThat(report.food.change).isAmountEqualTo(-1)
        assertThat(Model.food).isAmountEqualTo(0)
    }

    fun `Given no food big people When end turn Then people die`() {
        Model.food = Amount(0)
        Model.people = Amount(1)
        Model.player.buildings.houses.amount = Amount(1)
        Model.player.buildings.foodProducerAmountToZero()

        val report = execute()

        assertThat(report.people.change.real).isNegative()
        assertThat(Model.people.real).isLessThan(10)
    }

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

    private fun execute() = EndTurnExecutor(RandomServiceAlwaysMinimum).execute()

}
