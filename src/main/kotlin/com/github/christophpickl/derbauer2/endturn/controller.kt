package com.github.christophpickl.derbauer2.endturn

import com.github.christophpickl.derbauer2.endturn.happening.Happener
import com.github.christophpickl.derbauer2.model.Model
import mu.KotlinLogging.logger

class EndTurnController : EndTurnCallback {

    private val log = logger {}
    private val happener = Happener()

    override fun onEndTurn() {
        if (isGameOver()) {
            log.info { "game over" }
            Model.currentView = GameOverView()
            return
        }
        log.info { "end turn" }
        val happening = happener.letItHappen()
        if (happening != null) {
            log.info { "happening occured: $happening" }
            Model.currentView = happening
        } else {
            Model.goHome()
        }
    }

    private fun isGameOver() =
        Model.people <= 0

}

interface EndTurnCallback {
    fun onEndTurn()
}
