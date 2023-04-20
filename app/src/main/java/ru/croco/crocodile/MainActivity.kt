package ru.croco.crocodile

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlinx.android.synthetic.main.start.*
import androidx.preference.PreferenceManager


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.start)

        imageView.setBackgroundResource(R.drawable.main_animation)

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

        if (!isDatabaseInitializer) {
            val db = DatabaseHelper(this)
            print("loadDatabase")
            loadDatabase(db, this)
            sharedPref
                .edit()
                .putBoolean("ru.kongosha.friends.IsDatabaseInitialized", true)
                .apply()
        }
    }

companion object{
    fun loadDatabase(db: DatabaseHelper, context: Context) = run {
        val ins: InputStream = context.resources.openRawResource(
            context.resources.getIdentifier(
                "db",
                "raw",
                context.packageName
            )
        )
        db.insertFromFile(BufferedReader(InputStreamReader(ins)))
    }
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
            val intent = Intent(this, TeamNameActivity::class.java)
            val frameAnimation: AnimationDrawable = imageView.getBackground() as AnimationDrawable
            tap.animate().apply {
                tap.visibility = View.GONE
                duration = 650
                frameAnimation.start()
            }.withEndAction {
                frameAnimation.stop()
                startActivity(intent)
            }.start()
        }
    }

}