package com.github.christophpickl.derbauer.data

import com.github.christophpickl.derbauer.CHEAT_MODE
import com.github.christophpickl.derbauer.model.Amount

class ValuesResources {
    val gold = Amount(if (CHEAT_MODE) 900 else 300)
    val foodBuyPrice = Amount(4)
    val foodSellPrice = Amount(2)
    val food = Amount(if (CHEAT_MODE) 400 else 50)
    val people = Amount(if (CHEAT_MODE) 10 else 2)
    val land = Amount(if (CHEAT_MODE) 100 else 10)
    val landBuyPrice = Amount(50)
    val landSellPrice = Amount(40)
}

class ValuesBuildings {
    val houses = ValueBuilding(
        realAmount = if (CHEAT_MODE) 30 else 1,
        realLandNeeded = 1,
        realBuyPrice = 40
    )
    val housePeopleCapacity = Amount(10)
    val farms = ValueBuilding(
        realAmount = if (CHEAT_MODE) 10 else 1,
        realLandNeeded = 2,
        realBuyPrice = 70
    )
    val farmFoodProduction = Amount(4)
    val granaries = ValueBuilding(
        realAmount = if (CHEAT_MODE) 20 else 1,
        realLandNeeded = 1,
        realBuyPrice = 50
    )
    val granaryFoodCapacity = Amount(250)
    val barrack = ValueBuilding(
        realAmount = if (CHEAT_MODE) 3 else 0,
        realLandNeeded = 1,
        realBuyPrice = 100
    )
    val barrackMilitaryCapacity = Amount(10)
    val castles = ValueBuilding(
        realAmount = if (CHEAT_MODE) 1 else 0,
        realLandNeeded = 4,
        realBuyPrice = 300
    )
    val castlePeopleCapacity = Amount(80)
    val castleFoodCapacity = Amount(800)
}

class ValuesMilitaries {
    val soldiers = ValueMilitary(
        realAmount = if (CHEAT_MODE) 2 else 0,
        realBuyPrice = 20,
        attackModifier = 1.0,
        realCostsPeople = 1
    )
    val knights = ValueMilitary(
        realAmount = if (CHEAT_MODE) 0 else 0,
        realBuyPrice = 30,
        attackModifier = 1.2,
        realCostsPeople = 1
    )
    val catapults = ValueMilitary(
        realAmount = if (CHEAT_MODE) 0 else 0,
        realBuyPrice = 50,
        attackModifier = 1.4,
        realCostsPeople = 3
    )
    val attackBattleDelay = if (CHEAT_MODE) 400 else 600
}

class ValuesUpgrades {
    val farmProductivityBuyPrice = Amount(500)
    val increasePriceAfterBought = 2.0
    val farmProductionIncrease = 1L

    val militaryBuyPrice = Amount(400)
}

class ValuesAchievements {
    val trade1HistoryNeed = 10
    val trade1PriceModifier = 0.08
    val trade1GoldReward = Amount(300)
    val attack1HistoryNeed = 5
    val attack1AttackModifier = 0.2
}

class ValuesHappenings {
    val turnsCooldown = if (CHEAT_MODE) 1 else 10
    val baseProbability = if (CHEAT_MODE) 0.9 else 0.1
}

class ValuesActions {
    val visitorsWaitingInThroneRoom = if (CHEAT_MODE) 5 else 0
}

class ValuesFeatures {
    // upgrade
    val upgradePeopleNeeded = Amount(10)
    val foodProductionUpgradeBuildingsNeeded = Amount(10)
    val foodProductionUpgradeFoodNeeded = Amount(500)

    // building
    val castlePeopleNeeded = Amount(if (CHEAT_MODE) 10 else 50)

    // military
    val knightBarracksNeeded = Amount(if (CHEAT_MODE) 1 else 5)
    val catapultBarracksNeeded = Amount(if (CHEAT_MODE) 2 else 20)
}

class ValuesGlobals {
    val reproductionRate: Double = 0.1
    val peopleGoldRate: Double = 0.9
}

class ValuesKarma {
    val initial = 0.0
    val karmaTurnBalancer = 0.02
    val throneRoom = ValuesKarmaThroneRoom()
}

class ValuesKarmaThroneRoom {
    val boyMoneyLittle = 0.1
    val boyMoneyMedium = 0.2
    val boyMoneyMuch = 0.3
    val boySendAway = -0.1
    val boyThrowDungeon = -0.4

    val generalAgree = 0.3
    val generalDecline = -0.1
}

// =====================================================================================================================

object Values {
    val resources = ValuesResources()
    val buildings = ValuesBuildings()
    val militaries = ValuesMilitaries()
    val upgrades = ValuesUpgrades()
    val achievements = ValuesAchievements()
    val happenings = ValuesHappenings()
    val actions = ValuesActions()
    val features = ValuesFeatures()
    val globals = ValuesGlobals()
    val karma = ValuesKarma()
}

class ValueBuilding(
    realAmount: Long,
    realLandNeeded: Long,
    realBuyPrice: Long
) {
    val amount = Amount(realAmount)
    val landNeeded = Amount(realLandNeeded)
    val buyPrice = Amount(realBuyPrice)
}

class ValueMilitary(
    val realAmount: Long,
    val realBuyPrice: Long,
    val attackModifier: Double,
    val realCostsPeople: Long
) {
    val amount = Amount(realAmount)
    val buyPrice = Amount(realBuyPrice)
    val costsPeople = Amount(realCostsPeople)
}
