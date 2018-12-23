package com.github.christophpickl.derbauer

import com.github.christophpickl.derbauer.logic.screens.MainController
import com.github.christophpickl.derbauer.logic.service.AchievementHappener
import com.github.christophpickl.derbauer.logic.service.EndTurn
import com.github.christophpickl.derbauer.logic.service.EndTurnHappener
import com.github.christophpickl.derbauer.logic.service.Router
import com.github.christophpickl.derbauer.logic.service.ScreenControllerRegistry
import com.github.christophpickl.derbauer.view.Keyboard
import com.google.common.eventbus.EventBus
import com.google.inject.AbstractModule

class MainModule : AbstractModule() {
    override fun configure() {
        bind(EventBus::class.java).toInstance(EventBus())
        bind(Router::class.java).asEagerSingleton()
        bind(Keyboard::class.java).asEagerSingleton()
        bind(MainController::class.java).asEagerSingleton()
        bind(ScreenControllerRegistry::class.java).asEagerSingleton()
        bind(EndTurn::class.java).asEagerSingleton()
        bind(AchievementHappener::class.java).asEagerSingleton()
        bind(EndTurnHappener::class.java).asEagerSingleton()
    }
}
