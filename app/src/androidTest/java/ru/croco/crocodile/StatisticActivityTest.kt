package ru.croco.crocodile

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

class StatisticActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(StatisticActivity::class.java)

    @Test
    fun okButtonCheck() {
        Espresso.onView(ViewMatchers.withId(R.id.ok_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText("OK")))
    }

    @Test
    fun click() {
        Espresso.onView(ViewMatchers.withId(R.id.ok_button)).perform(ViewActions.click())
        intended(hasComponent(GameActivity::class.java.name))
    }



}