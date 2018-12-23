package com.github.christophpickl.derbauer.view

import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val mainTextArea by cssclass()

        val colorBackground = c("#C2B137")
        val colorFont = c("#000000")

    }

    init {
        mainTextArea {
            textFill = colorFont
            fontWeight = FontWeight.BOLD
            fontFamily = "Monaco"
            fontSize = 20.px
            content {
                backgroundColor += Styles.colorBackground
            }
        }
    }
}
