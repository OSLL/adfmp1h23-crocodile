package ru.croco.crocodile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.gameplay.*
import java.util.*
import kotlinx.android.synthetic.main.start.*
import kotlinx.android.synthetic.main.start.book
import kotlinx.android.synthetic.main.start.settings


class GameActivity : AppCompatActivity() {
    /**
     * Игровое поле
     */
    private val db = DatabaseHelper(this)
    var time: Long? = 60

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.gameplay)

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        functionalityOfButtons(sharedPref)
        val switchPref = sharedPref.getBoolean("example_switch", false)
//        sharedPref.edit().putBoolean("example_switch", false).apply()

    }

    fun functionalityOfButtons(sharedPref: SharedPreferences) = run {
        book.setOnClickListener {
            val intent = Intent(this, BookActivity::class.java)
            startActivity(intent)
        }

        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        var isGameFirst = true
        var playerBall1 = 0
        var playerBall2 = 0
        var isGameStarted = false
        player1_gameOnline.visibility = View.INVISIBLE
        player2_gameOnline.visibility = View.INVISIBLE

        start.setOnClickListener {
            time =
                try {
                    sharedPref.getString("time_settings", "60")?.toLong()
                } catch (e: Exception) {
                    60L
                }
            if (time == null) {
                time = 60L
            }
            val tipsSettings = sharedPref.getBoolean("example_switch", true)
            println(tipsSettings)
            if (!tipsSettings) {
                tips_of_word.visibility = View.INVISIBLE
            } else {
                tips_of_word.visibility = View.VISIBLE
            }

            val countDownTimer = object : CountDownTimer(time!! * 1000, 1000) {
                override fun onTick(millsUntilFinished: Long) {
                    val minutes = (millsUntilFinished / 1000) / 60
                    val seconds = (millsUntilFinished / 1000) % 60
                    if (isGameFirst) {
                        player1_timer.text = String.format("%d:%02d", minutes, seconds)
                        player2_timer.text = ""
                    } else {
                        player1_timer.text = ""
                        player2_timer.text = String.format("%d:%02d", minutes, seconds)
                    }
                }

                override fun onFinish() {
                    println("isGameFirst $isGameFirst")
                    isGameFirst = isGameFirst.not()
                    isGameStarted = false
                    start.visibility = View.VISIBLE
                    next_button.visibility = View.INVISIBLE
                    skip_button.visibility = View.INVISIBLE
                }
            }

            isGameStarted = isGameStarted.not()
            if (isGameStarted) {
                next_button.visibility = View.VISIBLE
                skip_button.visibility = View.VISIBLE
                start.visibility = View.INVISIBLE

                val random = Random()
                val randomInt: Int = random.nextInt(db.notesCount - 1) + 1
                var b = randomInt
                val note = db.getNote(b.toLong())
                main_word.text = note.word
                tips_of_word.text = note.description

                if (isGameFirst) {
                    player1_gameOnline.visibility = View.VISIBLE
                    player2_gameOnline.visibility = View.INVISIBLE
                } else {
                    player1_gameOnline.visibility = View.INVISIBLE
                    player2_gameOnline.visibility = View.VISIBLE
                }

                countDownTimer.start()
            } else {
                next_button.visibility = View.INVISIBLE
                skip_button.visibility = View.INVISIBLE
                start.visibility = View.VISIBLE

                player1_gameOnline.visibility = View.INVISIBLE
                player2_gameOnline.visibility = View.INVISIBLE
                countDownTimer.cancel()
                isGameFirst = isGameFirst.not()
            }
        }

        val random = Random()
        val randomInt: Int = random.nextInt(db.notesCount - 1) + 1
        var b = randomInt
        skip_button.setOnClickListener {
            if (isGameStarted) {
                val note = db.getNote(b.toLong())
                main_word.text = note.word
                tips_of_word.text = note.description

                b = random.nextInt(db.allNotes.size - 1) + 1
            }
            R.id.settings
        }

        next_button.setOnClickListener {
            if (isGameStarted) {
                val note = db.getNote(b.toLong())
                if (isGameFirst) {
                    playerBall1++
                    player1Tablo.text = "PLAYER1: ${playerBall1}"
                } else {
                    playerBall2++
                    player2Tablo.text = "PLAYER2: ${playerBall2}"
                }
                main_word.text = note.word
                tips_of_word.text = note.description
                b = random.nextInt(db.allNotes.size - 1) + 1
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            println("_______________________")
            println( data.getStringExtra("settings_tips"))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
