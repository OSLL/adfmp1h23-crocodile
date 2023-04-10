package ru.croco.crocodile

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.TypedValue
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.round.*


class RoundActivity : AppCompatActivity() {
    private val db = DatabaseHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("I WANNA CHECK THIS PROBLEM")
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.round)
        val roundWords = intent.getIntArrayExtra("roundWords")?.toSet()

        println("RoundWords $roundWords")

        if (roundWords != null) {
            for (idx in roundWords) {
                val note = db.getNote(idx.toLong())

                val checkBox = CheckBox(this)
                val layoutParams = ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT
                )
                checkBox.layoutParams = layoutParams
                checkBox.text = note.word
                checkBox.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f)
                checkBox.setTextColor(Color.BLUE)
                checkBox.isChecked = true
                linearLayout.addView(checkBox)
            }
        }

        ok_button.setOnClickListener {
            finish()
        }
    }
}