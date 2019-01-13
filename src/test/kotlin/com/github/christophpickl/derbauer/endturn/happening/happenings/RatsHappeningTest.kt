package com.github.christophpickl.derbauer.endturn.happening.happenings

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class RatsHappeningTest {

    fun `Given cooldown is zero When calc probability Then it is for sure`() {
        foodCapacityHalfwayFull()
        val happening = RatsHappening()
        happening.currentCooldownDays = 0

        val probability = happening.probability()

        assertThat(probability.value).isEqualTo(1.0)
    }

    fun `Given cooldown is maximum When calc probability Then it is for sure NOT`() {
        foodCapacityHalfwayFull()
        val happening = RatsHappening()
        happening.currentCooldownDays = happening.totalCooldownDays

        val probability = happening.probability()

        assertThat(probability.value).isEqualTo(0.0)
    }

    fun `Given cooldown is at half and full food capacity When calc probability Then it is greater than 50%`() {
        val happening = RatsHappening()
        happening.currentCooldownDays = happening.totalCooldownDays / 2
        Model.food = Model.totalFoodCapacity

        val probability = happening.probability()

        assertThat(probability.value).isGreaterThan(0.5)
    }

    fun `Given cooldown is at half and empty food capacity When calc probability Then it is less than 50%`() {
        val happening = RatsHappening()
        happening.currentCooldownDays = happening.totalCooldownDays / 2
        Model.food = Amount.zero

        val probability = happening.probability()

        assertThat(probability.value).isLessThan(0.5)
    }

    private fun foodCapacityHalfwayFull() {
        Model.food = Amount((Model.totalFoodCapacity.real / 2.0).toLong())
    }

}
