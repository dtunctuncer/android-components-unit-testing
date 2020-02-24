package com.dtunctuncer.testing

import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.runner.lifecycle.ActivityLifecycleCallback
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private val viewModel: MainViewModel = mockk(relaxUnitFun = true)
    private val activityLifecycleObserver =
        ActivityLifecycleCallback { activity, stage ->
            when (stage) {
                Stage.PRE_ON_CREATE -> {
                    if (activity is MainActivity) {
                        activity.viewModel = viewModel
                    }
                }
                else -> {
                    // no-op
                }
            }
        }

    @Before
    fun setUp() {
        ActivityLifecycleMonitorRegistry.getInstance()
            .addLifecycleCallback(activityLifecycleObserver)
    }

    @After
    fun tearDown() {
        ActivityLifecycleMonitorRegistry.getInstance()
            .removeLifecycleCallback(activityLifecycleObserver)
        scenario.close()
    }

    @Test
    fun `Starts without crashing`() {
        every { viewModel.saveButtonEnabled } returns true
        scenario = launchActivity()
    }

    @Test
    fun `Save button starts enabled`() {
        // Given
        every { viewModel.saveButtonEnabled } returns true

        // When
        scenario = launchActivity()

        // Then
        onView(withId(R.id.saveBtn)).check(matches(isEnabled()))
    }

    @Test
    fun `Save button clicked`() {
        // Given
        every { viewModel.saveButtonEnabled } returns true

        scenario = launchActivity()

        // When
        onView(withId(R.id.saveBtn)).perform(click())

        // Then
        verify { viewModel.saveButtonClicked() }
    }

    @Test
    fun `Save button can't clicked`() {
        // Given
        every { viewModel.saveButtonEnabled } returns false

        scenario = launchActivity()

        // When
        onView(withId(R.id.saveBtn)).perform(click())

        // Then
        verify(inverse = true) { viewModel.saveButtonClicked() }
    }
}