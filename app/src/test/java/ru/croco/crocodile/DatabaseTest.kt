package ru.croco.crocodile

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.croco.crocodile.GameActivity.Companion.getWordsFromDB

import ru.croco.crocodile.MainActivity.Companion.loadDatabase
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [31])
class DatabaseTest {

    private lateinit var db: DatabaseHelper

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = DatabaseHelper(context)
        context.resources
        loadDatabase(db, context)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testNote() {
        val r = db.getNote(1)
        Assert.assertEquals(r.word, "Absence")
        Assert.assertEquals(r.description, "The lack or unavailability of something or someone.")
    }

    @Test
    @Throws(Exception::class)
    fun testCount() {
        val countDb = db.notesCount
        Assert.assertEquals(countDb, 1100)
    }

    @Test
    fun getWordsFromDBTest(){
        val resDict = getWordsFromDB(10, db)
        Assert.assertEquals(resDict.size, 10)
        val resDict2 = getWordsFromDB(100, db)
        Assert.assertEquals(resDict2.size, 100)
    }
}

