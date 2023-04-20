package ru.croco.crocodile

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.widget.CheckBox
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.round.*
import java.util.HashMap


class RoundActivity : AppCompatActivity() {
    private val db = DatabaseHelper(this)
    private val generalResultUnchecked = mutableListOf<Int>()
    private val generalResultChecked = mutableListOf<Int>()
    private val checkboxes = mutableListOf<CheckBox>()

    private fun addToLinearLayout(res: List<CheckBox>){
        for (checkBox in res) {
            linearLayout.addView(checkBox)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.round)
        val roundWordsChecked = intent.getIntArrayExtra("roundWordsChecked")?.toMutableList()
        val roundWordsUnchecked = intent.getIntArrayExtra("roundWordsUnchecked")?.toMutableList()

        val res = createCheckboxes(this, roundWordsChecked, true, db)
        checkboxes.addAll(res)
        addToLinearLayout(res)
        val res2 = createCheckboxes(this, roundWordsUnchecked, false, db)
        checkboxes.addAll(res2)
        addToLinearLayout(res2)

        roundWordsUnchecked?.let { roundWordsChecked?.addAll(it) }

//
        ok_button.setOnClickListener {
            if (roundWordsChecked != null && roundWordsUnchecked != null) {
                for (i in 0 until roundWordsChecked.size) {
                    if (!checkboxes[i].isChecked) {
                        generalResultUnchecked.add(roundWordsChecked[i])
                    } else{
                        generalResultChecked.add(roundWordsChecked[i])
                    }
                }
            }
            intent.putExtra("uncheckedWords", generalResultUnchecked.toIntArray())
            intent.putExtra("checkedWords", generalResultChecked.toIntArray())
            setResult(10, intent)
            finish()
        }
    }
    companion object{
        fun createCheckboxes(context: Context, roundWords: List<Int>?, isChecked: Boolean, db: DatabaseHelper): MutableList<CheckBox> {
            val checkboxes = mutableListOf<CheckBox>()
            if (roundWords != null) {
                for (idx in roundWords) {
                    val note = db.getNote(idx.toLong())

                    val checkBox = CheckBox(context)
                    checkboxes.add(checkBox)
                    val layoutParams = ActionBar.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT
                    )
                    checkBox.layoutParams = layoutParams
                    checkBox.text = note.word
                    checkBox.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f)
                    checkBox.setTextColor(Color.BLUE)
                    checkBox.isChecked = isChecked
                }
            }
            return checkboxes
        }
    }
}
