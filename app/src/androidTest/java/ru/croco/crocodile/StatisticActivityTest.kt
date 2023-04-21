package ru.croco.crocodile

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import java.io.IOException

class StatisticActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(StatisticActivity::class.java)

    @Before
    fun initFunc() {
        Intents.init()
    }

    @After
    @Throws(IOException::class)
    fun afterFunc() {
        Intents.release()
    }

    @Test
    fun okButtonCheck() {
        Espresso.onView(ViewMatchers.withId(R.id.ok_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText("OK")))
    }

    @Test
    fun click() {
        Espresso.onView(ViewMatchers.withId(R.id.ok_button)).check(matches(isClickable()))
    }
}