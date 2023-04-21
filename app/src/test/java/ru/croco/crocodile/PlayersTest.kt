package ru.croco.crocodile

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.test.core.app.ApplicationProvider
import com.orbitalsonic.sonictimer.SonicCountDownTimer
import kotlinx.android.synthetic.main.gameplay.*
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.croco.crocodile.GameActivity.Companion.getGeneralTeam
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [31])
class PlayersTest {
    private lateinit var db: DatabaseHelper

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
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
    fun isPlayingTeamOddFalse() {
        val isOddGame = false
        val teams = mutableListOf("team1", "team2")
        val r = getGeneralTeam(isOddGame, teams)
        Assert.assertEquals("team2", r)
    }

    @Test
    fun isPlayingTeamOddTrue() {
        val isOddGame = true
        val teams = mutableListOf("team1", "team2")
        val r = getGeneralTeam(isOddGame, teams)
        Assert.assertEquals("team1", r)
    }

    @Test
    fun timerCheckTest() {
        val time = 30L
        val myTimer = object : SonicCountDownTimer(time * 1000, 1000) {

            override fun onTimerTick(timeRemaining: Long) {
                val minutes = (timeRemaining / 1000) / 60
                val seconds = (timeRemaining / 1000) % 60
            }

            @SuppressLint("SetTextI18n")
            override fun onTimerFinish() {

            }
        }

        assertEquals(myTimer.isTimerRunning(), false)
        myTimer.startCountDownTimer()
        assertEquals(myTimer.isTimerRunning(), true)
    }


}