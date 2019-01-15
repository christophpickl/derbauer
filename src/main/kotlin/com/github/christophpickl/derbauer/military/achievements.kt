package com.github.christophpickl.derbauer.military

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.endturn.achievement.AbstractAchievement
import com.github.christophpickl.derbauer.endturn.achievement.Achievement
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType

@Suppress("unused")
class MilitaryAchievements {
    val attack1 = Attack1Achievement()

    @get:JsonIgnore val all get() = propertiesOfType<MilitaryAchievements, Achievement>(this)
}

class Attack1Achievement : AbstractAchievement(label = "Military Mastery I: Stronger armies") {
    override fun condition() = Model.history.attacksWon >= Values.achievements.attack1AttacksWonNeeded
    override fun execute() {
        Model.player.armies.all.forEach {
            it.attackModifier += Values.achievements.attack1AttackModifier
        }
    }
}
