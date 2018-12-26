package com.github.christophpickl.derbauer.endturn

import com.github.christophpickl.derbauer.RandomServiceMinimum
import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.build._foodProducerAmountToZero
import com.github.christophpickl.derbauer.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class EndTurnExecutorTest {

    fun `Given small food big people When end turn Then food is empty`() {
        Model.food = 1
        Model.people = 2
        Model.player.buildings.houses.amount = 1
        Model.player.buildings._foodProducerAmountToZero()

        val report = execute()

        assertThat(report.food.change).isEqualTo(-1)
        assertThat(Model.food).isEqualTo(0)
    }

    fun `Given no food big people When end turn Then people die`() {
        Model.food = 0
        Model.people = 1
        Model.player.buildings.houses.amount = 1
        Model.player.buildings._foodProducerAmountToZero()

        val report = execute()

        assertThat(report.people.change).isNegative()
        assertThat(Model.people).isLessThan(10)
    }

    private fun execute() = EndTurnExecutor(RandomServiceMinimum).execute()

}