package com.github.christophpickl.derbauer2.feature

import com.github.christophpickl.derbauer2.Values
import com.github.christophpickl.derbauer2.build.FoodCapacityBuilding
import com.github.christophpickl.derbauer2.build.FoodProducingBuilding
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.model.amountsSum

class UpgradeFeature {

    private val upgradeCondition = FeatureCondition {
        Model.people >= Values.features.upgradePeopleNeeded
    }
    val isUpgradeEnabled get() = upgradeCondition.checkAndGet()

    private val foodProductionUpgradeCondition = FeatureCondition {
        (Model.player.buildings.filterAll<FoodProducingBuilding>().amountsSum() +
            Model.player.buildings.filterAll<FoodCapacityBuilding>().amountsSum() >=
            Values.features.foodProductionUpgradeBuildingsNeeded)
            && Model.food >= Values.features.foodProductionUpgradeFoodNeeded
    }
    val isFoodProductionUpgradeEnabled get() = foodProductionUpgradeCondition.checkAndGet()

}


