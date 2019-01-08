package com.github.christophpickl.derbauer.endturn.happening

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.model.Percent
import com.github.christophpickl.derbauer.ui.view.View
import com.github.christophpickl.kpotpourri.common.random.RandomServiceAlwaysMinimum
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class HappenerTest {

    private lateinit var positiveHappening1: TestHappening
    private lateinit var positiveHappening2: TestHappening
    private lateinit var negativeHappening: TestHappening

    @BeforeMethod
    fun `init state`() {
        positiveHappening1 = TestHappening(
            viewText = "positive1",
            currentCooldownDays = 0,
            nature = HappeningNature.Positive,
            probability = Percent(1.0)
        )
        positiveHappening2 = TestHappening(
            viewText = "positive2",
            currentCooldownDays = 0,
            nature = HappeningNature.Positive,
            probability = Percent(0.8)
        )
        negativeHappening = TestHappening(
            viewText = "negative",
            currentCooldownDays = 0,
            nature = HappeningNature.Negative,
            probability = Percent(1.0)
        )
    }

    fun `Given no karma When let it happen Then return negative happening`() {
        Model.global.karma = 0.0

        val view = happen()

        assertThat(view).isNotNull
        assertThat(view!!.renderContent).isEqualTo(negativeHappening.viewText)
    }

    fun `Given full karma When let it happen Then return positive happening`() {
        Model.global.karma = 1.0

        val view = happen()

        assertThat(view).isNotNull
        assertThat(view!!.renderContent).isEqualTo(positiveHappening1.viewText)
    }

    private fun happen(): View? {
        val happener = Happener(
            random = RandomServiceAlwaysMinimum,
            happenings = listOf(
                positiveHappening1,
                positiveHappening2,
                negativeHappening
            )
        )
        return happener.letItHappen()
    }

}

class TestHappening(
    val viewText: String,
    override val currentCooldownDays: Int,
    override val nature: HappeningNature,
    private val probability: Percent,
    var wasExecuted: Boolean = false
) : Happening {

    override fun probability() = probability

    override fun coolUpWarmUp() {
    }

    override fun execute(): String {
        wasExecuted = true
        return viewText
    }

}
