package com.github.christophpickl.derbauer.endturn

import com.github.christophpickl.derbauer.endturn.happening.Happener
import com.github.christophpickl.derbauer.misc.NotificationsView
import com.github.christophpickl.derbauer.model.Model
import mu.KotlinLogging.logger

class EndTurnController : EndTurnCallback {

    private val log = logger {}
    private val happener = Happener()

    override fun onEndTurn() {
        log.info { "onEndTurn()" }
        if (isGameOver()) {
            log.info { "game over" }
            Model.currentView = GameOverView()
            return
        }

        val notifications = Model.notifications.consumeAll()
        if (notifications.isNotEmpty()) {
            Model.currentView = NotificationsView(notifications.sorted())
            return
        }
        
        val happening = happener.letItHappen()
        if (happening != null) {
            log.info { "happening occured: $happening" }
            Model.currentView = happening
        } else {
            Model.goHome()
        }
    }

    private fun isGameOver() =
        Model.people <= 0 || Model.land <= 0

}

interface EndTurnCallback {
    fun onEndTurn()
}
