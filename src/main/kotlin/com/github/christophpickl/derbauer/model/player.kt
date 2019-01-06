package com.github.christophpickl.derbauer.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.building.Buildings
import com.github.christophpickl.derbauer.military.Armies
import com.github.christophpickl.derbauer.resource.Resources
import com.github.christophpickl.derbauer.upgrade.Upgrades
import com.github.christophpickl.kpotpourri.common.math.KMath
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType

data class Player(
    val resources: Resources = Resources(),
    val buildings: Buildings = Buildings(),
    val upgrades: Upgrades = Upgrades(),
    val armies: Armies = Armies()
) {
    @get:JsonIgnore val all get() = propertiesOfType<Player, PlayerEntity>(this)

    val wealth: Amount get() = all.sumBy { it.wealth }

    fun relativeWealthBy(percentage: Double): Amount {
        require(percentage > 0.0 && percentage < 1.0)
        var calc = (wealth.real * percentage).toLong()
        val digits = calc.toString().length - 1
        val limiter = Math.pow(10.0, digits.toDouble()).toLong()
        calc = calc / limiter * limiter
        return Amount(KMath.max(0L, calc))
    }
}

interface PlayerEntity : Wealthable
