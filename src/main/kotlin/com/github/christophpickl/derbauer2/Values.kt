package com.github.christophpickl.derbauer2

class ValuesResources {
    val gold = if (CHEAT_MODE) 900 else 100
    val food = if (CHEAT_MODE) 400 else 300
    val people = if (CHEAT_MODE) 9 else 2
    val land = if (CHEAT_MODE) 100 else 5
}

class ValuesBuildings {
    val houses = ValueBuilding(
        amount = if (CHEAT_MODE) 30 else 1,
        landNeeded = 1,
        buyPrice = 15
    )
    val granaries = ValueBuilding(
        amount = if (CHEAT_MODE) 20 else 1,
        landNeeded = 1,
        buyPrice = 30
    )
    val farms = ValueBuilding(
        amount = if (CHEAT_MODE) 10 else 1,
        landNeeded = 2,
        buyPrice = 50
    )
    val castles = ValueBuilding(
        amount = if (CHEAT_MODE) 0 else 0,
        landNeeded = 4,
        buyPrice = 200
    )
}

class ValuesMilitaries {
    val soldiers = ValueMilitary(
        amount = if (CHEAT_MODE) 2 else 0,
        buyPrice = 20,
        attackModifier = 1.0,
        costsPeople = 1
    )
    val knights = ValueMilitary(
        amount = if (CHEAT_MODE) 0 else 0,
        buyPrice = 30,
        attackModifier = 1.2,
        costsPeople = 1
    )
    val catapults = ValueMilitary(
        amount = if (CHEAT_MODE) 0 else 0,
        buyPrice = 50,
        attackModifier = 1.4,
        costsPeople = 3
    )
    val attackBattleDelay = if (CHEAT_MODE) 400 else 600
}

class ValuesUpgrades {
    val increasePriceAfterBought = 2.0
    val farmBuyPrice = 250
    val farmProductionIncrease = 1
}

class ValuesAchievements {
    val trade1HistoryNeed = 10
    val trade1PriceModifier = 0.1
    val attack1HistoryNeed = 5
    val attack1AttackModifier = 0.2
}

class ValuesHappenings {
    val turnsCooldown = 10
    val baseProbability = 0.1
}

class ValuesActions {
    val visitorsWaitingInThroneRoom = if (CHEAT_MODE) 5 else 0
}

class ValuesFeatures {
    val castlePeopleNeeded = if (CHEAT_MODE) 9 else 100
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
}

class ValueBuilding(
    val amount: Int,
    val landNeeded: Int,
    val buyPrice: Int
)

class ValueMilitary(
    val amount: Int,
    val buyPrice: Int,
    val attackModifier: Double,
    val costsPeople: Int
)
