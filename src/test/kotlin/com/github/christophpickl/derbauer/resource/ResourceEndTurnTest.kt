package com.github.christophpickl.derbauer.resource

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.building.foodCapacityAmountToZero
import com.github.christophpickl.derbauer.building.foodProducerAmountToZero
import com.github.christophpickl.derbauer.isAmountEqualTo
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import com.github.christophpickl.kpotpourri.common.random.RandomServiceAlwaysMinimum
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class ResourceEndTurnTest {

    // FOOD ============================================================================================================

    fun `Given some food and some people When end turn Then each people eats one food`() {
        granaryAndHouseExists()
        Model.food = Amount(10)
        Model.people = Amount(2)
        Model.player.buildings.foodProducerAmountToZero()

        val report = execute()

        assertThat(report.foodIncome).isAmountEqualTo(-Model.people.real)
    }

    fun `Given less food then people eat When end turn Then all food is eaten`() {
        granaryAndHouseExists()
        val foodAmount = 2L
        Model.food = Amount(foodAmount)
        Model.people = Amount(20)
        Model.player.buildings.foodProducerAmountToZero()

        val report = execute()

        assertThat(report.foodIncome).isAmountEqualTo(-foodAmount)
    }

    fun `Given food at maximum capacity and one farm When end turn Then no food is produced`() {
        granaryAndHouseExists()
        Model.food = Model.totalFoodCapacity
        Model.people = Amount(1)
        Model.player.buildings.farms.amount = Amount.one

        val report = execute()

        assertThat(report.foodIncome).isAmountEqualTo(0)
    }

    fun `Given more food than capacity When end turn Then 20% from total capacity of food is lost`() {
        Model.player.buildings.foodCapacityAmountToZero()
        Model.player.buildings.granaries.amount = Amount.one
        Model.food = Model.totalFoodCapacity + 100

        val report = execute()

        assertThat(report.foodIncome).isAmountEqualTo(-(Model.totalFoodCapacity.real / 100.0 * 20.0).toLong())
    }

    // PEOPLE ==========================================================================================================

    fun `Given no food but some people When end turn Then people die`() {
        granaryAndHouseExists()
        Model.player.buildings.foodProducerAmountToZero()
        Model.food = Amount(0)
        Model.people = Amount(1)

        val report = execute()

        assertThat(report.peopleIncome).isAmountEqualTo(-1)
    }

    fun `Given more people than capacity When end turn Then 20% from total capacity of people die`() {
        granaryAndHouseExists()
        Model.player.buildings.houses.amount = Amount.one
        Model.food = Model.totalFoodCapacity
        Model.people = Amount(100)

        val report = execute()

        assertThat(report.peopleIncome).isAmountEqualTo(-(Model.totalPeopleCapacity.real / 100.0 * 20.0).toLong())
    }

    fun `Given no food and over capacity When end turn Then lots of people die`() {
        granaryAndHouseExists()
        Model.player.buildings.foodProducerAmountToZero()
        Model.food = Amount(0)
        Model.people = Amount(100)

        val report = execute()

        // 8 ... no food, 2 ... over capacity, 1 ... random kills
        assertThat(report.peopleIncome).isAmountEqualTo(-(8 + 2 + 1))
    }

    // GOLD ============================================================================================================

    fun `Given some people When end turn Then gold produced by people times rate times random`() {
        granaryAndHouseExists()
        Model.people = Amount(100)

        val report = execute()

        assertThat(report.goldIncome).isAmountEqualTo((Model.people.real * Model.global.peopleGoldRate * 0.8).toLong())
    }

    // TEST INFRA ======================================================================================================

    private fun execute() = ResourceEndTurn(RandomServiceAlwaysMinimum).endTurn()

    private fun granaryAndHouseExists() {
        Model.player.buildings.granaries.amount = Amount(1)
        Model.player.buildings.houses.amount = Amount(1)
    }

}
