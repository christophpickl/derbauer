package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.derbauer.army.ArmyController
import com.github.christophpickl.derbauer.build.BuildController
import com.github.christophpickl.derbauer.misc.MainController
import com.github.christophpickl.derbauer.trade.TradeController
import com.github.christophpickl.derbauer.upgrade.UpgradeController
import javax.inject.Inject

@Deprecated(message = "v2")
class ScreenControllerRegistry @Inject constructor(

    val main: MainController,
    val trade: TradeController,
    val build: BuildController,
    val upgrade: UpgradeController,
    val army: ArmyController

)
