package com.github.christophpickl.derbauer.endturn

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.build.foodProducerAmountToZero
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

    private fun execute() = EndTurnExecutor(RandomServiceAlwaysMinimum).execute()

}
