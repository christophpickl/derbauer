package com.github.christophpickl.derbauer2.military

import com.github.christophpickl.derbauer2.TestModelListener
import com.github.christophpickl.derbauer2.build.MilitaryCapacityBuilding
import com.github.christophpickl.derbauer2.hasSameAmountAs
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.ui.Renderer
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class MilitaryTest {

    private val renderer = mock<Renderer>()

    fun `Given all good but no military cap When hire Then not possible and dont hire`() {
        val soldier = Model.player.militaries.soldiers
        Model.gold = soldier.buyPrice
        Model.people = soldier.costsPeople
        Model.player.buildings.all.filterIsInstance<MilitaryCapacityBuilding>().forEach { it.amount = 0 }

        assertThat(soldier.effectiveBuyPossibleAmount).isEqualTo(0)
        MilitaryController(renderer).doHire(soldier, 1)

        soldier hasSameAmountAs 0
    }

    fun `Given all good When hire Then possible and hire`() {
        Model.player.upgrades.militaryUpgrade.setToMaxLevel()
        val soldier = Model.player.militaries.soldiers
        Model.gold = soldier.buyPrice
        Model.people = soldier.costsPeople + 1
        Model.player.buildings.barracks.amount = 1

        assertThat(soldier.effectiveBuyPossibleAmount).isEqualTo(1)
        MilitaryController(renderer).doHire(soldier, 1)

        soldier hasSameAmountAs 1
    }

}
