package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.derbauer.logic.screens.Screen

class GameState {

    lateinit var screen: Screen
    var day = 1
    val prompt = Prompt()
    val player = Player()
    val prizes = Prizes()

    val affordableLand get() = player.gold / prizes.landBuy
    val affordableFood get() = player.gold / prizes.foodBuy

    override fun toString() = "State{day=$day, screen=${screen.javaClass.simpleName}, player=$player}"
}

class Prizes {
    val landBuy = 20
    val landSell = 15
    val foodBuy = 4
    val foodSell = 2
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

class Player {
    var gold = 100
    val goldFormatted get() = ResourceMeta.formatGold(gold)
    val goldAll get() = Triple(gold, goldFormatted, ResourceMeta.goldDigits)

    var land = 5
    val landFormatted get() = ResourceMeta.formatLand(land)
    val landAll get() = Triple(land, landFormatted, ResourceMeta.landDigits)

    var food = 300
    val foodFormatted get() = ResourceMeta.formatFood(food)
    val foodAll get() = Triple(food, foodFormatted, ResourceMeta.foodDigits)

    var people = 10
    val peopleFormatted get() = ResourceMeta.formatPeople(people)
    val peopleAll get() = Triple(people, peopleFormatted, ResourceMeta.peopleDigits)

    val buildings = Buildings()

    override fun toString() = "Player{gold=$gold, land=$land, food=$food, people=$people, buildings=$buildings}"
}

class Buildings {
    var farms = 1

    override fun toString() = "Buildings{farms=$farms}"
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
