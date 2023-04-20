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
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.ar.core.Config
import org.junit.After
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoundActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(RoundActivity::class.java)

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

//    @Test
//    fun checkboxIsClick() {
//        val i = Intent()
//        onView(withId(R.id.ok_button))
//            .check(matches(isDisplayed()))
//            .check(matches(withText("OK")))
//    }
}