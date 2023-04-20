package ru.croco.crocodile

import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.google.ar.core.Config
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoundActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(RoundActivity::class.java)

    private lateinit var db: DatabaseHelper

    @Before
    fun createDb() {
        Intents.init()
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = DatabaseHelper(context)
        MainActivity.loadDatabase(db, context)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        Intents.release()
        db.close()
    }

    @Test
    fun okButtonCheck() {
        onView(withId(R.id.ok_button))
            .check(matches(isDisplayed()))
            .check(matches(withText("OK")))
    }

    @Test
    fun checkboxIsClick() {
        val i = Intent()
        val roundWordsChecked = mutableListOf(1,2)
        val roundWordsUnchecked = mutableListOf(3,4)
        i.putExtra("roundWordsChecked", roundWordsChecked.toIntArray())
        i.putExtra("roundWordsUnchecked", roundWordsUnchecked.toIntArray())
        activityRule.launchActivity(i)
        onView(withText("Absence"))
            .check(matches(isChecked()))
            .perform(click())
            .check(matches(not(isChecked())))

        onView(withText("Answer"))
            .check(matches(not(isChecked())))
            .perform(click())
            .check(matches(isChecked()))

        onView(withId(R.id.ok_button))
            .check(matches(isDisplayed()))
            .check(matches(withText("OK")))
    }
}