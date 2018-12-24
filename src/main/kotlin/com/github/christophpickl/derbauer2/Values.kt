package com.github.christophpickl.derbauer2

val VALUES = if (CHEAT_MODE) CheatValues else DefaultValues

interface Values {
    val gold: Int
    val food: Int
    val people: Int
    val land: Int

    val houses: Int
    val granaries: Int
    val farms: Int
    val castles: Int

    val achievementTrade1HistoryNeed: Int
    val achievementTrade1PriceModifier: Double
    val achievementAttack1HistoryNeed: Int
    val achievementAttack1AttackModifier: Double

    val happeningTurnsCooldown: Int
    val happeningBaseProbability: Double

    val featureCastlePeopleNeeded: Int
}

object DefaultValues : Values {
    override val gold = 100
    override val food = 300
    override val people = 2
    override val land = 5

    override val houses = 1
    override val granaries = 1
    override val farms = 1
    override val castles = 0

    override val achievementTrade1HistoryNeed = 10
    override val achievementTrade1PriceModifier = 0.1
    override val achievementAttack1HistoryNeed = 5
    override val achievementAttack1AttackModifier = 0.2

    override val happeningTurnsCooldown = 10
    override val happeningBaseProbability = 0.1

    override val featureCastlePeopleNeeded = 100
}

object CheatValues : Values {
    override val gold = 900
    override val food = 400
    override val people = 9
    override val land = 100

    override val houses = 30
    override val granaries = 10
    override val farms = 20
    override val castles = 0

    override val achievementTrade1HistoryNeed = 1
    override val achievementTrade1PriceModifier = 0.2
    override val achievementAttack1HistoryNeed = 1
    override val achievementAttack1AttackModifier = 0.4

    override val happeningTurnsCooldown = 10
    override val happeningBaseProbability = 0.1

    override val featureCastlePeopleNeeded = 10

}
