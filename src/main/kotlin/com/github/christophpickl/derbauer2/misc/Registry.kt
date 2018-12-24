package com.github.christophpickl.derbauer2.misc

import com.github.christophpickl.derbauer2.domain.HomeController
import com.github.christophpickl.derbauer2.domain.HomeScreenCallback
import com.github.christophpickl.derbauer2.domain.TradeCallback
import com.github.christophpickl.derbauer2.domain.TradeController

interface ScreenCallback :
    HomeScreenCallback,
    TradeCallback

class Registry(
    private val homeController: HomeController = HomeController(),
    private val tradeController: TradeController = TradeController()
) : ScreenCallback,
    HomeScreenCallback by homeController,
    TradeCallback by tradeController
