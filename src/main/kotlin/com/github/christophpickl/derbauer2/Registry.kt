package com.github.christophpickl.derbauer2

import com.github.christophpickl.derbauer2.build.BuildCallback
import com.github.christophpickl.derbauer2.build.BuildController
import com.github.christophpickl.derbauer2.trade.TradeCallback
import com.github.christophpickl.derbauer2.trade.TradeController

interface ScreenCallback :
    HomeScreenCallback,
    TradeCallback,
    BuildCallback
// army
// upgrade

class Registry(
    private val homeController: HomeController = HomeController(),
    private val tradeController: TradeController = TradeController(),
    private val buildController: BuildController = BuildController()
) : ScreenCallback,
    HomeScreenCallback by homeController,
    TradeCallback by tradeController,
    BuildCallback by buildController
