package com.github.christophpickl.derbauer.ui.view

import com.github.christophpickl.derbauer.TestModelListener
import com.github.christophpickl.derbauer.ViewCallback
import com.github.christophpickl.derbauer.model.Model
import com.github.christophpickl.derbauer.ui.Beeper
import com.github.christophpickl.derbauer.ui.PromptInput
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Listeners
import org.testng.annotations.Test

@Test
@Listeners(TestModelListener::class)
class ChooseViewTest {

    private lateinit var beeper: Beeper
    private lateinit var callback: ViewCallback
    private val anyChoices = listOf(TestableChoice())
    private val anyCancelSupport = CancelSupport.Disabled
    private val zeroChoice = TestableChoice(zeroChoice = true)
    private val nonZeroChoice = TestableChoice(zeroChoice = false)

    @BeforeMethod
    fun `init mocks`() {
        beeper = mock()
        callback = mock()
    }

    fun `Given cancel disabled When input empty Then beep`() {
        val view = TestableChooseView(anyChoices, CancelSupport.Disabled, beeper)

        view.onCallback(callback, PromptInput.Empty)

        verify(beeper, times(1)).beep(any())
    }


    fun `Given cancel enabled with targetView When input empty Then set current view to targetView and dont beep`() {
        val targetView = mock<View>()
        val view = TestableChooseView(anyChoices, CancelSupport.Enabled { targetView }, beeper)

        view.onCallback(callback, PromptInput.Empty)

        assertThat(Model.currentView).isSameAs(targetView)
        verify(beeper, never()).beep(any())
    }

    fun `Given non-zero choice When input is valid Then call callback and dont beep`() {
        val view = TestableChooseView(listOf(nonZeroChoice), anyCancelSupport, beeper)

        view.onCallback(callback, PromptInput.Number(1))

        assertThat(view.choices).containsExactly(nonZeroChoice)
        verify(beeper, never()).beep(any())
    }

    fun `Given non-zero choice When input is invalid Then dont call callback and beep`() {
        val view = TestableChooseView(listOf(nonZeroChoice), anyCancelSupport, beeper)

        view.onCallback(callback, PromptInput.Number(2))

        assertThat(view.choices).isEmpty()
        verify(beeper, times(1)).beep(any())
    }

    fun `Given zero choice When input is valid Then call callback and dont beep`() {
        val view = TestableChooseView(listOf(nonZeroChoice, zeroChoice), anyCancelSupport, beeper)

        view.onCallback(callback, PromptInput.Number(1))

        assertThat(view.choices).containsExactly(nonZeroChoice)
        verify(beeper, never()).beep(any())
    }

    fun `Given zero choice When input is invalid Then dont call callback and beep`() {
        val view = TestableChooseView(listOf(zeroChoice), anyCancelSupport, beeper)

        view.onCallback(callback, PromptInput.Number(1))

        assertThat(view.choices).isEmpty()
        verify(beeper, times(1)).beep(any())
    }

    fun `Given zero choice When input is 0 Then call callback with zero choice`() {
        val view = TestableChooseView(listOf(zeroChoice), anyCancelSupport, beeper)

        view.onCallback(callback, PromptInput.Number(0))

        assertThat(view.choices).containsExactly(zeroChoice)
        verify(beeper, never()).beep(any())
    }

}

private class TestableChooseView(
    choices: List<TestableChoice>,
    cancelSupport: CancelSupport,
    beeper: Beeper
) : ChooseView<TestableChoice>(
    message = "message",
    choices = choices,
    cancelSupport = cancelSupport,
    beeper = beeper
) {
    val choices = mutableListOf<TestableChoice>()
    override fun onCallback(callback: ViewCallback, choice: TestableChoice) {
        choices += choice
    }

}

private data class TestableChoice(
    override val label: String = "anyLabel",
    private val zeroChoice: Boolean = false
) : Choice {
    override fun isZeroChoice() = zeroChoice
}
