package com.github.christophpickl.derbauer.logic

import com.github.christophpickl.derbauer.achievement.AchievementHappener
import com.github.christophpickl.derbauer.happening.Happener
import com.github.christophpickl.derbauer.misc.EndTurn
import com.github.christophpickl.derbauer.misc.MainController
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
        bind(Happener::class.java).asEagerSingleton()
    }
}
