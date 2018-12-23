package com.github.christophpickl.derbauer

import com.github.christophpickl.derbauer.logic.AchievementHappener
import com.github.christophpickl.derbauer.logic.EndTurnHappener
import com.github.christophpickl.derbauer.logic.Router
import com.github.christophpickl.derbauer.logic.ScreenControllerRegistry
import com.github.christophpickl.derbauer.logic.TurnFinisher
import com.github.christophpickl.derbauer.logic.screens.MainController
import com.github.christophpickl.derbauer.model.State
import com.github.christophpickl.derbauer.view.Keyboard
import com.google.common.eventbus.EventBus
import com.google.inject.AbstractModule

class MainModule : AbstractModule() {
    override fun configure() {
        bind(EventBus::class.java).toInstance(EventBus())
        bind(State::class.java).asEagerSingleton()
        bind(Router::class.java).asEagerSingleton()
        bind(Keyboard::class.java).asEagerSingleton()
        bind(MainController::class.java).asEagerSingleton()
        bind(ScreenControllerRegistry::class.java).asEagerSingleton()
        bind(TurnFinisher::class.java).asEagerSingleton()
        bind(AchievementHappener::class.java).asEagerSingleton()
        bind(EndTurnHappener::class.java).asEagerSingleton()
    }
}
