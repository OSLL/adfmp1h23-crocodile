package ru.croco.crocodile

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.TypedValue
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.round.linearLayout
import kotlinx.android.synthetic.main.statistic.*

class StatisticActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.statistic)

        val roundResults = intent.getIntArrayExtra("roundResults")
        val teamName1 = intent.getStringExtra("teamName1")
        val teamName2 = intent.getStringExtra("teamName2")
        val generalResult1 = intent.getIntExtra("teamName1Res", 0)
        val generalResult2 = intent.getIntExtra("teamName2Res", 0)
        player1Result.text = generalResult1.toString()
        player2Result.text = generalResult2.toString()

        var isOdd = true
        var roundNumberOdd = 1
        var roundNumberNoOdd = 1
        if (roundResults != null) {
            for (result in roundResults) {
                val textView = TextView(this)
                if (isOdd) {
                    textView.text = "Round $roundNumberOdd $teamName1 : $result"
                    roundNumberOdd++
                } else {
                    textView.text = "Round $roundNumberNoOdd $teamName2 : $result"
                    roundNumberNoOdd++
                }
                isOdd = isOdd.not()
                val layoutParams = ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT
                )
                textView.layoutParams = layoutParams
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f)
                textView.setTextColor(Color.BLUE)
                linearLayout.addView(textView)
            }
        }

        ok_button.setOnClickListener{
            finish()
        }

    }
}