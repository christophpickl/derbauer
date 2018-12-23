package com.github.christophpickl.derbauer.view

import com.github.christophpickl.derbauer.logic.GameEngine
import javafx.scene.control.TextArea
import tornadofx.*

class MainViewController : Controller() {

    private val view: MainView by inject()
    private val engine: GameEngine by di()
    private val keyboard: Keyboard by di()
    private val renderer: GameRenderer by di()

    init {
//        view.mainTextArea.text = renderer.render()
//        view.mainTextArea.keyboard {
//            addEventHandler(KeyEvent.KEY_RELEASED) { keyboard.onReleased(it) }
//        }
    }
}

class MainView : View() {

    private lateinit var mainTextArea: TextArea

    override val root = borderpane {
        center {
            mainTextArea = textarea {
                isEditable = false
                addClass(Styles.mainTextArea)
                text = "hallo"
            }
        }
    }
}
