package com.github.christophpickl.derbauer2

import com.github.christophpickl.derbauer2.build.BuildCallback
import com.github.christophpickl.derbauer2.build.BuildController
import com.github.christophpickl.derbauer2.endturn.EndTurnCallback
import com.github.christophpickl.derbauer2.endturn.EndTurnController
import com.github.christophpickl.derbauer2.home.HomeController
import com.github.christophpickl.derbauer2.home.HomeScreenCallback
import com.github.christophpickl.derbauer2.military.MilitaryCallback
import com.github.christophpickl.derbauer2.military.MilitaryController
import com.github.christophpickl.derbauer2.trade.TradeCallback
import com.github.christophpickl.derbauer2.trade.TradeController
import com.github.christophpickl.derbauer2.ui.Renderer
import com.github.christophpickl.derbauer2.upgrade.UpgradeCallback
import com.github.christophpickl.derbauer2.upgrade.UpgradeController

interface ScreenCallback :
    HomeScreenCallback,
    TradeCallback,
    BuildCallback,
    UpgradeCallback,
    MilitaryCallback,
    EndTurnCallback

class Registry(
    renderer: Renderer,
    private val homeController: HomeController = HomeController(),
    private val tradeController: TradeController = TradeController(),
    private val buildController: BuildController = BuildController(),
    private val upgradeController: UpgradeController = UpgradeController(),
    private val militaryController: MilitaryController = MilitaryController(renderer),
    private val endTurnController: EndTurnController = EndTurnController()
) : ScreenCallback,
    HomeScreenCallback by homeController,
    TradeCallback by tradeController,
    BuildCallback by buildController,
    UpgradeCallback by upgradeController,
    MilitaryCallback by militaryController,
    EndTurnCallback by endTurnController
