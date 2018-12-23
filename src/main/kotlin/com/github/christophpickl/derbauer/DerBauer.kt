package com.github.christophpickl.derbauer

import com.github.christophpickl.derbauer.view.DerBauerFxApp
import javafx.application.Application

object DerBauer {
    @JvmStatic
    fun main(args: Array<String>) {
        Application.launch(DerBauerFxApp::class.java, *args)
    }
}
