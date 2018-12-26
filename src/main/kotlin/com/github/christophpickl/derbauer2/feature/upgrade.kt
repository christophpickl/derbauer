package com.github.christophpickl.derbauer2.feature

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.christophpickl.derbauer2.build.FoodCapacityBuilding
import com.github.christophpickl.derbauer2.build.FoodProducingBuilding
import com.github.christophpickl.derbauer2.data.Values
import com.github.christophpickl.derbauer2.misc.propertiesOfType
import com.github.christophpickl.derbauer2.model.Conditional
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.amountsSum
import com.github.christophpickl.derbauer2.upgrade.Upgrade

class UpgradeFeatures {

    val menu = AbstractFeature("New possibility available: Upgrade") {
        Model.people >= Values.features.upgradePeopleNeeded
    }

    val foodProductivityUpgrade = UpgradeFeature(Model.player.upgrades.farmProductivity) {
        (Model.player.buildings.filterAll<FoodProducingBuilding>().amountsSum() +
            Model.player.buildings.filterAll<FoodCapacityBuilding>().amountsSum() >=
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
