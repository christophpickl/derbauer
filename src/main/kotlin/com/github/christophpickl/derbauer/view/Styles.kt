package com.github.christophpickl.derbauer.view

import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val mainTextArea by cssclass()

        val colorBlack = c("#000000")
        val colorGreen = c("#4DFF52")

    }

    init {
        mainTextArea {
            textFill = colorGreen
            fontFamily = "Monaco"
            fontSize = 20.px
            content {
                backgroundColor += Styles.colorBlack
            }
        }
    }
}

