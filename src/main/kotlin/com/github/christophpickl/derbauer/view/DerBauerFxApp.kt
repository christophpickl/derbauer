package com.github.christophpickl.derbauer.view

import com.github.christophpickl.derbauer.MainModule
import com.google.inject.Guice
import tornadofx.*
import kotlin.reflect.KClass

class DerBauerFxApp : App(
    primaryView = MainView::class,
    stylesheet = Styles::class
) {
    private val guice = Guice.createInjector(MainModule())

    init {
        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>) = guice.getInstance(type.java)
        }
        registerEagerSingletons()
    }

    private fun registerEagerSingletons() {
//        find(MainViewController::class)
    }
}
