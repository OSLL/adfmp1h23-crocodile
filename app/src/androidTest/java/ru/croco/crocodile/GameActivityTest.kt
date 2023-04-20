package ru.croco.crocodile

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class GameActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(GameActivity::class.java)

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
    fun countWordsTabloCheck() {
        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog()) // <---
            .check(matches(isDisplayed()))
            .perform(ViewActions.click())
        onView(withId(R.id.countWordsTablo)).check(matches(withText("Count words: 50")))
    }

    @Test
    fun playButtonStartGame(){
        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog()) // <---
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.play_button)).perform(click())

        onView(withId(R.id.skipButton)).check(matches(isDisplayed()))
        onView(withId(R.id.crocoButton)).check(matches(isDisplayed()))
    }

    @Test
    fun playButtonInPauseGame(){
        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog()) // <---
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.skipButton)).check(matches(not(isDisplayed())))
        onView(withId(R.id.crocoButton)).check(matches(not(isDisplayed())))

        onView(withId(R.id.play_button))
            .perform(click())

        onView(withId(R.id.book)).check(matches(not(isDisplayed())))
        onView(withId(R.id.settings)).check(matches(not(isDisplayed())))
        onView(withId(R.id.play_button)).perform(click())

        onView(withId(R.id.skipButton)).check(matches(not(isDisplayed())))
        onView(withId(R.id.crocoButton)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testUntilFinishRoundActiveGame(){
        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog()) // <---
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.play_button))
            .perform(click())

        waitViewShown(withText("OK"))
        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
        intended(IntentMatchers.hasComponent(RoundActivity::class.java.name))
    }
    @Test
    fun fixUpButtonGame(){
        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog()) // <---
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.play_button))
            .perform(click())

        waitViewShown(withText("OK"))
        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
        intended(IntentMatchers.hasComponent(RoundActivity::class.java.name))

        onView(withId(R.id.ok_button))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.skipButton))
            .check(matches(isDisplayed()))
            .check(matches(withText("FIX UP")))
            .perform(click())
    }

    @Test
    fun crocoButtonGame(){
        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog()) // <---
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.play_button))
            .perform(click())

        onView(withId(R.id.crocoButton))
            .check(matches(withText("CROCO")))
            .check(matches(isClickable()))

        onView(withId(R.id.skipButton))
            .check(matches(withText("SKIP")))
            .check(matches(isClickable()))
    }

    @Test
    fun statisticButtonFinishGame(){
        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog()) // <---
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.play_button))
            .perform(click())

        onView(withId(R.id.crocoButton))
            .check(matches(withText("CROCO")))
            .check(matches(isClickable()))
            .perform(click())
            .perform(click())
            .perform(click())
            .perform(click())
            .perform(click())

        waitViewShown(withText("OK"))
        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.crocoButton))
            .check(matches(withText("STATISTIC")))
            .perform(click())
        intended(IntentMatchers.hasComponent(StatisticActivity::class.java.name))
    }

    @Test
    fun bookClick(){
        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog()) // <---
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.book))
            .perform(click())
        intended(IntentMatchers.hasComponent(BookActivity::class.java.name))
    }

    @Test
    fun settingsClick(){
        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog()) // <---
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.settings))
            .perform(click())
        Intents.intended(IntentMatchers.hasComponent(SettingsActivity::class.java.name))
    }

    fun waitViewShown(matcher: Matcher<View>) {
        val idlingResource = ViewShownIdlingResource(matcher)
        try {
            IdlingRegistry.getInstance().register(idlingResource)
            onView(matcher).check(matches(isDisplayed()))
            onView(withId(0)).check(doesNotExist())
        } finally {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }
}