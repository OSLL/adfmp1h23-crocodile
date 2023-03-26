package ru.croco.crocodile

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlinx.android.synthetic.main.start.*
import androidx.preference.PreferenceManager


class MainActivity : AppCompatActivity() {
    private lateinit var rocketAnimation: AnimationDrawable
    val db = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.start)

        imageView.apply {
            setBackgroundResource(R.drawable.main_animation)
            rocketAnimation = background as AnimationDrawable
        }
        creatorDatabase()
        functionalityOfButtons()
    }

    private fun creatorDatabase() = run {

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        val sharedPref = this.getSharedPreferences(
            "ru.kongosha.friends", MODE_PRIVATE
        )

        val isDatabaseInitializer = sharedPref
            .getBoolean("ru.kongosha.friends.IsDatabaseInitialized", false)
//        loadDatabase()
        if (!isDatabaseInitializer) {
            print("loadDatabase")
            loadDatabase()
            sharedPref
                .edit()
                .putBoolean("ru.kongosha.friends.IsDatabaseInitialized", true)
                .apply()
        }
    }

    private fun loadDatabase() = run {
        val ins: InputStream = resources.openRawResource(
            resources.getIdentifier(
                "db",
                "raw",
                this.packageName
            )
        )
        db.insertFromFile(BufferedReader(InputStreamReader(ins)))
    }

    private fun functionalityOfButtons() = run {

        book.setOnClickListener {
            val intent = Intent(this, BookActivity::class.java)
            startActivity(intent)
        }

        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent, 0)
        }

        startButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)

            tap.animate().apply {
                tap.visibility = View.GONE
                duration = 650
                rocketAnimation.start()
            }.withEndAction {
                rocketAnimation.stop()
                startActivity(intent)
            }.start()
        }
    }

}