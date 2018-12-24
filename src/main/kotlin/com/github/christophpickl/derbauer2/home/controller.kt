package com.github.christophpickl.derbauer2.home

import com.github.christophpickl.derbauer2.build.BuildScreen
import com.github.christophpickl.derbauer2.endturn.EndTurnExecutor
import com.github.christophpickl.derbauer2.endturn.EndTurnScreen
import com.github.christophpickl.derbauer2.endturn.achievement.AchievementChecker
import com.github.christophpickl.derbauer2.misc.enforceWhenBranches
import com.github.christophpickl.derbauer2.model.Model
import com.github.christophpickl.derbauer2.trade.TradeScreen

class HomeController : HomeScreenCallback {
    override fun goEndTurnReport() {
        val message = EndTurnExecutor.execute()
        Model.screen = EndTurnScreen(message)
    }

    override fun onHomeEnum(choice: HomeChoice) {
        when (choice.enum) {
            HomeEnum.Trade -> {
                Model.screen = TradeScreen()
            }
            HomeEnum.Build -> {
                Model.screen = BuildScreen()
            }
            HomeEnum.EndTurn -> {
                val achievement = AchievementChecker.nextScreen()
                if (achievement != null) {
                    Model.screen = achievement
                } else {
                    goEndTurnReport()
                }
            }
        }.enforceWhenBranches()
    }

}
