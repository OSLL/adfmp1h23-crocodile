package ru.croco.crocodile

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import java.io.IOException

class TeamNameActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(TeamNameActivity::class.java)

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
    fun checkTeamNames() {
        onView(withId(R.id.editTextTeam1)).perform(
            clearText(),
            typeText("My team1"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.editTextTeam2)).perform(
            clearText(),
            typeText("My team2"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.editTextTeam1)).check(matches(withText("My team1")))
        onView(withId(R.id.editTextTeam2)).check(matches(withText("My team2")))
    }

    @Test
    fun checkCountWordsTime() {
        onView(withId(R.id.countWords)).perform(
            clearText(),
            typeText("10"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.time)).perform(
            clearText(),
            typeText("100"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.countWords)).check(matches(withText("10")))
        onView(withId(R.id.time)).check(matches(withText("100")))
    }

    @Test
    @Throws(IOException::class)
    fun checkCountWordsErrorTime() {
        onView(withId(R.id.countWords)).perform(
            clearText(),
            typeText("Error"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.time)).perform(
            clearText(),
            typeText("Error"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.countWords)).check(matches(withText("")))
        onView(withId(R.id.time)).check(matches(withText("")))
    }
}
