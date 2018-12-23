package com.github.christophpickl.derbauer.view

import com.github.christophpickl.derbauer.logic.screens.HomeScreen
import com.github.christophpickl.derbauer.model.State
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javafx.scene.control.TextArea
import javafx.scene.input.KeyEvent
import mu.KotlinLogging.logger
import tornadofx.*

object RenderEvent

class MainView : View() {

    lateinit var mainTextArea: TextArea

    override val root = borderpane {
        title = "Der Bauer"
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

class MainViewFxController : Controller() {

    private val logg = logger {}
    private val view: MainView by inject()
    private val keyboard: Keyboard by di()
    private val renderer: Renderer by di()
    private val state: State by di()
    private val bus: EventBus by di()

    init {
        bus.register(this)
        state.screen = HomeScreen(state)
        onRenderEvent()
        view.root.addEventFilter(KeyEvent.ANY) { event ->
            keyboard.onKeyEvent(event)
        }
    }

    @Subscribe
    fun onRenderEvent(@Suppress("UNUSED_PARAMETER") event: RenderEvent = RenderEvent) {
        logg.trace("onRenderEvent()")
        val root = Exception("Just to get the stacktrace")
        runLater {
            // assure running on UI thread
            try {
                view.mainTextArea.text = renderer.render()
            } catch (e: Exception) {
                logg.error(e) { "Unexpected exception while rendering." }
                logg.error(root) { "Root cause." }

            }
        }
    }

}
