package ru.croco.crocodile

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.croco.crocodile.RoundActivity.Companion.createCheckboxes
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [31])
class RoundTest {
    private lateinit var db: DatabaseHelper
    private lateinit var context: Context

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext()
        db = DatabaseHelper(context)
        context.resources
        MainActivity.loadDatabase(db, context)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun isCreateCheckboxes() {
        val roundWords = GameActivity.getWordsFromDB(10, db).keys.toList()
        val listOfElements = createCheckboxes(context, roundWords, false, db)
        assertEquals(listOfElements.size, 10)
        assertEquals(listOfElements[0].isChecked, false)
        assertEquals(listOfElements[0].textSize, 30f)
    }
}
