package com.github.christophpickl.derbauer

import javafx.application.Application
import javafx.scene.control.TextArea
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val mainTextArea by cssclass()

        val colorBlack = c("#000000")
        val colorGreen = c("#4DFF52")

    }

    init {
        mainTextArea {
            //            backgroundColor += colorBlack
            textFill = colorGreen
            fontFamily = "Monaco"
            fontSize = 20.px
            content {
                backgroundColor += Styles.colorBlack
            }
        }
    }
}

class MainView : View() {

//    private val partnersView: PartnersView by inject()

    private lateinit var mainTextArea: TextArea

    override val root = borderpane {
        center {
            mainTextArea = textarea {
                isEditable = false
                addClass(Styles.mainTextArea)
                text = "Hallo der Bauer"
            }
        }
    }
}

class DerBauerFxApp : App(
    primaryView = MainView::class,
    stylesheet = Styles::class
) {

}

object DerBauer {
    @JvmStatic
    fun main(args: Array<String>) {
        Application.launch(DerBauerFxApp::class.java, *args)
    }
}
