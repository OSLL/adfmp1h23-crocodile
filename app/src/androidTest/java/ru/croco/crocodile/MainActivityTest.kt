package ru.croco.crocodile

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.VerificationMode
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun startTest() {
        Intents.init()
    }

    @After
    @Throws(IOException::class)
    fun finishTest() {
        Intents.release()
    }

    @Test
    fun startButtonTest() {
        onView(withId(R.id.startButton))
            .perform(click())
        activityRule.launchActivity(Intent())
        Intents.intended(IntentMatchers.hasComponent(TeamNameActivity::class.java.name))
    }

    @Test
    fun settingsClick() {
        onView(withId(R.id.settings))
            .perform(click())
        Intents.intended(IntentMatchers.hasComponent(SettingsActivity::class.java.name))
    }

    @Test
    fun bookClick() {
        onView(withId(R.id.book))
            .perform(click())
        Intents.intended(IntentMatchers.hasComponent(BookActivity::class.java.name))
    }
}