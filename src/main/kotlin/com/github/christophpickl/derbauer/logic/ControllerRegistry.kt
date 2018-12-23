package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.derbauer.logic.screens.ArmyController
import com.github.christophpickl.derbauer.logic.screens.BuildController
import com.github.christophpickl.derbauer.logic.screens.MainController
import com.github.christophpickl.derbauer.logic.screens.TradeController
import com.github.christophpickl.derbauer.logic.screens.UpgradeController
import javax.inject.Inject

class ScreenControllerRegistry @Inject constructor(

    val main: MainController,
    val trade: TradeController,
    val build: BuildController,
    val upgrade: UpgradeController,
    val army: ArmyController

)
