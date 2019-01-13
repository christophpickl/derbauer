package com.github.christophpickl.derbauer.action.throneroom

import com.github.christophpickl.derbauer.assertAverage
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.amount.Amount
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test
class ThroneRoomEndTurnTest {

    private val throneRoom get () = Model.actions.throneRoom
    private val castles get () = Model.player.buildings.castles

    fun `Given no castles When end turn Then no visitors`() {
        throneRoom.visitorsWaiting = 0
        castles.amount = Amount.zero

        endTurn()

        assertThat(throneRoom.visitorsWaiting).isEqualTo(0)
    }

    fun `Given one castle When end turn Then one visitor`() {
        throneRoom.visitorsWaiting = 0
        castles.amount = Amount.one

        endTurn()

        assertThat(throneRoom.visitorsWaiting).isEqualTo(1)
    }

    fun `Given already max visitors When end turn Then visitors stay`() {
        castles.amount = Amount.one
        throneRoom.visitorsWaiting = throneRoom.maxVisitorsWaiting

        endTurn()

        assertThat(throneRoom.visitorsWaiting).isEqualTo(throneRoom.maxVisitorsWaiting)
    }

    fun `Given one castle When end turn a lot of times Then most always get one new visitor`() {
        castles.amount = Amount.one

        assertAverage {
            throneRoom.visitorsWaiting = 0
            endTurn()
            throneRoom.visitorsWaiting
        }

            .isEqualTo(1.0)
    }

    @DataProvider
    fun provideGrowthVisitors() = arrayOf(
        arrayOf(0, 0),
        arrayOf(1, 1),
        arrayOf(9, 1),
        arrayOf(10, 2),
        arrayOf(99, 2),
        arrayOf(100, 3),
        arrayOf(999, 3),
        arrayOf(1_000, 4)
    )

    @Test(dataProvider = "provideGrowthVisitors")
    fun `Given x castles When end turn Then expect y new visitors`(givenCastles: Long, expectedNewVisitors: Long) {
        Model.player.buildings.castles.amount = Amount(givenCastles)
        throneRoom.visitorsWaiting = 0

        endTurn()

        assertThat(throneRoom.visitorsWaiting).isEqualTo(expectedNewVisitors)
    }

    @DataProvider
    fun provideMaxVisitors() = provideGrowthVisitors().map { row ->
        row.copyOf().also { it[1] *= 3 }
    }.toTypedArray()

    @Test(dataProvider = "provideMaxVisitors")
    fun `Given x castles When get max visitors Then expect it to be 3 times the growth`(givenCastles: Long, expectedMaxVisitors: Long) {
        Model.player.buildings.castles.amount = Amount(givenCastles)

        val actual = throneRoom.maxVisitorsWaiting

        assertThat(actual).isEqualTo(expectedMaxVisitors)
    }

    private fun endTurn() {
        ThroneRoomEndTurn().onEndTurn()
    }
}
