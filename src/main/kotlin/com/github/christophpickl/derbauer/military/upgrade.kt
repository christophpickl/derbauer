package com.github.christophpickl.derbauer.military

import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Conditional
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.upgrade.AbstractUpgrade

class MilitaryUpgrade : AbstractUpgrade(
    label = "Military Expertise",
    buyPrice = Values.upgrades.militaryBuyPrice,
    maxLevel = 1
), Conditional {
    override val description get() = "enables military actions"
    override fun checkCondition() = Model.features.upgrade.militaryUpgrade.isEnabled()
    override fun execute() {} // dynamically checked in feature via this level
}
