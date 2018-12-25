package com.github.christophpickl.derbauer2.endturn

import com.github.christophpickl.derbauer2.TestModelListener
import com.github.christophpickl.derbauer2.build.foodProducerAmountToZero
import com.github.christophpickl.derbauer2.model.Model
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
        Model.player.buildings.foodProducerAmountToZero()

        val report = EndTurnExecutor.execute()

        assertThat(report.food.change).isEqualTo(-1)
        assertThat(Model.food).isEqualTo(0)
    }

    fun `Given no food big people When end turn Then people die`() {
        Model.food = 0
        Model.people = 1
        Model.player.buildings.houses.amount = 1
        Model.player.buildings.foodProducerAmountToZero()

        val report = EndTurnExecutor.execute()

        assertThat(report.people.change).isNegative()
        assertThat(Model.people).isLessThan(10)
    }

}
