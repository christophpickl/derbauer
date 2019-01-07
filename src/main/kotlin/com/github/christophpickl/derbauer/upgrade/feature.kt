package com.github.christophpickl.derbauer.upgrade

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.building.FoodCapacityBuilding
import com.github.christophpickl.derbauer.building.FoodProducingBuilding
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.misc.AbstractFeature
import com.github.christophpickl.derbauer.misc.Feature
import com.github.christophpickl.derbauer.model.Conditional
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.realAmountSum
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType

class UpgradeFeatures {

    val menu = AbstractFeature("New menu available: Upgrade") {
        all.any { it.isEnabled() }
    }

    val militaryUpgrade = UpgradeFeature(Model.player.upgrades.militaryUpgrade) {
        Model.land >= Values.features.militaryUpgradeLandNeeded
    }
    
    val foodProductivityUpgrade = UpgradeFeature(Model.player.upgrades.farmProductivity) {
        (Model.player.buildings.filterAll<FoodProducingBuilding>().realAmountSum() +
            Model.player.buildings.filterAll<FoodCapacityBuilding>().realAmountSum() >=
            Values.features.foodProductionUpgradeBuildingsNeeded)
            && Model.food >= Values.features.foodProductionUpgradeFoodNeeded
    }

    @JsonIgnore val all = propertiesOfType<UpgradeFeatures, Feature>(this)

}

class UpgradeFeature<U>(
    upgrade: U,
    predicate: () -> Boolean
) : AbstractFeature(
    notification = "New upgrade available: ${upgrade.label}",
    predicate = predicate
) where U : Upgrade, U : Conditional
