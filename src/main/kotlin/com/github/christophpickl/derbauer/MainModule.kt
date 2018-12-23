package com.github.christophpickl.derbauer

import com.github.christophpickl.derbauer.logic.GameEngine
import com.github.christophpickl.derbauer.logic.GameState
import com.github.christophpickl.derbauer.logic.MainScreenController
import com.github.christophpickl.derbauer.logic.ScreenControllerRegistry
import com.github.christophpickl.derbauer.view.Keyboard
import com.google.common.eventbus.EventBus
import com.google.inject.AbstractModule

class MainModule : AbstractModule() {
    override fun configure() {
        bind(EventBus::class.java).toInstance(EventBus())
        bind(GameState::class.java).asEagerSingleton()
        bind(GameEngine::class.java).asEagerSingleton()
        bind(Keyboard::class.java).asEagerSingleton()
        bind(MainScreenController::class.java).asEagerSingleton()
        bind(ScreenControllerRegistry::class.java).asEagerSingleton()
        
    }
}
