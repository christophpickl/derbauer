package com.github.christophpickl.derbauer

import com.github.christophpickl.derbauer.action.ActionCallback
import com.github.christophpickl.derbauer.action.ActionController
import com.github.christophpickl.derbauer.building.BuildCallback
import com.github.christophpickl.derbauer.building.BuildController
import com.github.christophpickl.derbauer.endturn.EndTurnCallback
import com.github.christophpickl.derbauer.endturn.EndTurnController
import com.github.christophpickl.derbauer.home.HomeCallback
import com.github.christophpickl.derbauer.home.HomeController
import com.github.christophpickl.derbauer.military.MilitaryCallback
import com.github.christophpickl.derbauer.military.MilitaryController
import com.github.christophpickl.derbauer.military.attack.AttackCallback
import com.github.christophpickl.derbauer.military.attack.AttackController
import com.github.christophpickl.derbauer.trade.TradeCallback
import com.github.christophpickl.derbauer.trade.TradeController
import com.github.christophpickl.derbauer.ui.Renderer
import com.github.christophpickl.derbauer.upgrade.UpgradeCallback
import com.github.christophpickl.derbauer.upgrade.UpgradeController

interface ViewCallback :
    HomeCallback,
    TradeCallback,
    BuildCallback,
    UpgradeCallback,
    MilitaryCallback,
    AttackCallback,
    ActionCallback,
    EndTurnCallback

class Registry(
    renderer: Renderer,
    private val homeController: HomeController = HomeController(),
    private val tradeController: TradeController = TradeController(),
    private val buildController: BuildController = BuildController(),
    private val upgradeController: UpgradeController = UpgradeController(),
    private val militaryController: MilitaryController = MilitaryController(),
    private val attackController: AttackController = AttackController(renderer),
    private val actionController: ActionController = ActionController(),
    private val endTurnController: EndTurnController = EndTurnController()
) : ViewCallback,
    HomeCallback by homeController,
    TradeCallback by tradeController,
    BuildCallback by buildController,
    UpgradeCallback by upgradeController,
    MilitaryCallback by militaryController,
    AttackCallback by attackController,
    ActionCallback by actionController,
    EndTurnCallback by endTurnController
