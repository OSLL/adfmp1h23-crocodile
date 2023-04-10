package ru.croco.crocodile

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.startmeet.*

class TeamNameActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.startmeet)

        startButton2.setOnClickListener {
            val i = Intent(this, GameActivity::class.java)
            i.putExtra("teamName1", editTextTeam1.text.toString())
            i.putExtra("teamName2", editTextTeam2.text.toString())
            i.putExtra("countWords", countWords.text.toString().toInt())
            i.putExtra("timerTime", time.text.toString().toLong())
            startActivity(i)
        }
    }
}