package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.derbauer.logic.screens.BuySellResourcesController
import com.github.christophpickl.derbauer.logic.screens.MainController
import javax.inject.Inject

class ScreenControllerRegistry @Inject constructor(

    val main: MainController,
    val buySell: BuySellResourcesController

)
