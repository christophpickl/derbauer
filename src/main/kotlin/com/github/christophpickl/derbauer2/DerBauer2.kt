package com.github.christophpickl.derbauer2

import com.github.christophpickl.derbauer2.misc.Keyboard
import com.github.christophpickl.derbauer2.misc.MainFrame
import com.github.christophpickl.derbauer2.misc.MainTextArea
import com.github.christophpickl.derbauer2.misc.Prompt
import com.github.christophpickl.derbauer2.misc.Renderer
import com.github.christophpickl.derbauer2.misc.Router

object DerBauer2 {
    @JvmStatic
    fun main(args: Array<String>) {
        State.reset()

        val prompt = Prompt()
        val text = MainTextArea()
        val keyboard = Keyboard()

        val renderer = Renderer(text, prompt)
        val engine = Router(renderer)

        text.addKeyListener(keyboard)
        keyboard.subscription.add(prompt)
        prompt.subscription.add(engine)

        renderer.render()
        MainFrame().buildAndShow(text)
    }
}
