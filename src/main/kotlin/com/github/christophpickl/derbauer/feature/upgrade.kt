package com.github.christophpickl.derbauer.feature

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer.build.FoodCapacityBuilding
import com.github.christophpickl.derbauer.build.FoodProducingBuilding
import com.github.christophpickl.derbauer.data.Values
import com.github.christophpickl.derbauer.model.Conditional
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.realAmountSum
import com.github.christophpickl.derbauer.upgrade.Upgrade
import com.github.christophpickl.kpotpourri.common.reflection.propertiesOfType

class UpgradeFeatures {

    val menu = AbstractFeature("New menu available: Upgrade") {
        Model.people >= Values.features.upgradePeopleNeeded
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
