package com.github.christophpickl.derbauer.endturn

import com.github.christophpickl.derbauer.model.Amount
import com.github.christophpickl.derbauer.model.Model

class EndTurnReport(
    goldIncome: Amount,
    foodIncome: Amount,
    peopleIncome: Amount,
    val notifications: List<String>
) {
    val gold = EndTurnReportLine(
        newValue = Model.gold,
        change = goldIncome,
        oldValue = Model.gold - goldIncome
    )
    val food = EndTurnReportLine(
        newValue = Model.food,
        change = foodIncome,
        oldValue = Model.food - foodIncome
    )
    val people = EndTurnReportLine(
        newValue = Model.people,
        change = peopleIncome,
        oldValue = Model.people - peopleIncome
    )
}

class EndTurnReportLine(
    val oldValue: Amount,
    val newValue: Amount,
    val change: Amount
)
