@file:Suppress("ConstantConditionIf")

package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.derbauer.logic.screens.Screen

private const val CHEAT_MODE = true

class State {

    lateinit var screen: Screen
    var day = 1
    val prompt = Prompt()
    val player = Player()
    val prizes = Prizes()
    val buildings = BuildingsMeta()
    val system = SystemMeta()

    val affordableLand get() = player.gold / prizes.landBuy
    val affordableFood get() = player.gold / prizes.foodBuy

    val playerPeopleMax get() = player.buildings.houses * buildings.houseCapacity

    override fun toString() = "State{day=$day, screen=${screen.javaClass.simpleName}, buildings=$buildings, player=$player}"

    init {
        reset()
    }

    fun reset() {
        day = 1
        player.reset()
        prizes.reset()
        buildings.reset()
        system.reset()
    }
}

class SystemMeta {
    var reproductionRate = 0.0

    fun reset() {
        reproductionRate = 0.1
    }
}

class BuildingsMeta {
    var houseCapacity = 0
    var farmProduces = 0

    fun reset() {
        houseCapacity = 5
        farmProduces = 2
    }

    override fun toString() = "BuildingsMeta{houseCapacity=$houseCapacity, farmProduces=$farmProduces}"
}

class Player {
    var gold = 0
    val goldFormatted get() = ResourceMeta.formatGold(gold)
    val goldAll get() = Triple(gold, goldFormatted, ResourceMeta.goldDigits)

    var land = 0
    val landFormatted get() = ResourceMeta.formatLand(land)
    val landAll get() = Triple(land, landFormatted, ResourceMeta.landDigits)

    var food = 0
    val foodFormatted get() = ResourceMeta.formatFood(food)
    val foodAll get() = Triple(food, foodFormatted, ResourceMeta.foodDigits)

    var people = 0
    val peopleFormatted get() = ResourceMeta.formatPeople(people)
    val peopleAll get() = Triple(people, peopleFormatted, ResourceMeta.peopleDigits)

    val buildings = Buildings()

    override fun toString() = "Player{gold=$gold, land=$land, food=$food, people=$people, buildings=$buildings}"

    fun reset() {
        gold = if (CHEAT_MODE) 500 else 100
        land = 5
        food = if (CHEAT_MODE) 2 else 300
        people = 2

        buildings.reset()
    }
}

class Prizes {
    var landBuy = 0
    var landSell = 0
    var foodBuy = 0
    var foodSell = 0
    var farm = 0
    var house = 0
    val upgrades = UpgradePrizes()

    fun reset() {
        landBuy = 20
        landSell = 15
        foodBuy = 4
        foodSell = 2
        farm = 40
        house = 25
        upgrades.reset()
    }
}

class UpgradePrizes {
    var farmProductivity = 0

    fun reset() {
        farmProductivity = 250
    }
}

object ResourceMeta {
    const val foodDigits = 4
    const val goldDigits = 4
    const val landDigits = 4
    const val peopleDigits = 3
    fun formatFood(food: Int) = formatNumber(food, foodDigits)
    fun formatGold(gold: Int) = formatNumber(gold, goldDigits)
    fun formatLand(land: Int) = formatNumber(land, landDigits)
    fun formatPeople(people: Int) = formatNumber(people, peopleDigits)

}

class Buildings {
    var farms = 0
    var houses = 0

    val totalCount get() = farms + houses

    fun formatAll() = listOf("Houses: $houses", "Farms: $farms")
    override fun toString() = "Buildings{farms=$farms}"
    fun reset() {
        farms = 1
        houses = 1
    }
}

class Prompt {

    private var _enteredText = ""
    val enteredText get() = _enteredText

    fun append(text: Char) {
        _enteredText = "$_enteredText$text"
    }

    fun clear() {
        _enteredText = ""
    }

    fun maybeRemoveLast(): Boolean {
        if (_enteredText.isEmpty()) {
            return false
        }
        _enteredText = _enteredText.substring(0, _enteredText.length - 1)
        return true
    }

}
