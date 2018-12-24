package com.github.christophpickl.derbauer2

import com.github.christophpickl.derbauer2.domain.HomeController
import com.github.christophpickl.derbauer2.domain.HomeScreenCallback
import com.github.christophpickl.derbauer2.trade.TradeCallback
import com.github.christophpickl.derbauer2.trade.TradeController

interface ScreenCallback :
    HomeScreenCallback,
    TradeCallback
// army
// upgrade

class Registry(
    private val homeController: HomeController = HomeController(),
    private val tradeController: TradeController = TradeController()
) : ScreenCallback,
    HomeScreenCallback by homeController,
    TradeCallback by tradeController
