package com.github.christophpickl.derbauer2

import com.github.christophpickl.derbauer2.action.ActionCallback
import com.github.christophpickl.derbauer2.action.ActionController
import com.github.christophpickl.derbauer2.build.BuildCallback
import com.github.christophpickl.derbauer2.build.BuildController
import com.github.christophpickl.derbauer2.endturn.EndTurnCallback
import com.github.christophpickl.derbauer2.endturn.EndTurnController
import com.github.christophpickl.derbauer2.home.HomeCallback
import com.github.christophpickl.derbauer2.home.HomeController
import com.github.christophpickl.derbauer2.military.MilitaryCallback
import com.github.christophpickl.derbauer2.military.MilitaryController
import com.github.christophpickl.derbauer2.trade.TradeCallback
import com.github.christophpickl.derbauer2.trade.TradeController
import com.github.christophpickl.derbauer2.ui.Renderer
import com.github.christophpickl.derbauer2.upgrade.UpgradeCallback
import com.github.christophpickl.derbauer2.upgrade.UpgradeController

interface ViewCallback :
    HomeCallback,
    TradeCallback,
    BuildCallback,
    UpgradeCallback,
    MilitaryCallback,
    ActionCallback,
    EndTurnCallback

class Registry(
    renderer: Renderer,
    private val homeController: HomeController = HomeController(),
    private val tradeController: TradeController = TradeController(),
    private val buildController: BuildController = BuildController(),
    private val upgradeController: UpgradeController = UpgradeController(),
    private val militaryController: MilitaryController = MilitaryController(renderer),
    private val actionController: ActionController = ActionController(),
    private val endTurnController: EndTurnController = EndTurnController()
) : ViewCallback,
    HomeCallback by homeController,
    TradeCallback by tradeController,
    BuildCallback by buildController,
    UpgradeCallback by upgradeController,
    MilitaryCallback by militaryController,
    ActionCallback by actionController,
    EndTurnCallback by endTurnController
