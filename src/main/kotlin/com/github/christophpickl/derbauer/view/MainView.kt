package com.github.christophpickl.derbauer.view

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javafx.scene.control.TextArea
import javafx.scene.input.KeyEvent
import mu.KotlinLogging.logger
import tornadofx.*

object RenderEvent

class MainViewController : Controller() {

    private val logg = logger {}
    private val view: MainView by inject()
    private val keyboard: Keyboard by di()
    private val renderer: Renderer by di()
    private val bus: EventBus by di()

    init {
        bus.register(this)
        onRerenderEvent()
        view.root.addEventFilter(KeyEvent.ANY) { event ->
            keyboard.onKeyEvent(event)
        }
    }

    @Subscribe
    fun onRerenderEvent(@Suppress("UNUSED_PARAMETER") event: RenderEvent = RenderEvent) {
        logg.debug("onRerenderEvent()")
        view.mainTextArea.text = renderer.render()
    }

}

class MainView : View() {

    lateinit var mainTextArea: TextArea

    override val root = borderpane {
        prefWidth = 1235.0
        prefHeight = 825.0
        center {
            mainTextArea = textarea {
                addClass(Styles.mainTextArea)
                isEditable = false
            }
        }
    }
}
