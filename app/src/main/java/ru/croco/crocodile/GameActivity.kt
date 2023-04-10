package ru.croco.crocodile

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.orbitalsonic.sonictimer.SonicCountDownTimer
import kotlinx.android.synthetic.main.gameplay.*
import kotlinx.android.synthetic.main.start.book
import kotlinx.android.synthetic.main.start.settings
import kotlinx.android.synthetic.main.startmeet.*
import java.util.*


class GameActivity : AppCompatActivity() {
    /**
     * Игровое поле
     */
    private val db = DatabaseHelper(this)
    var time: Long = 10

    var roundWords = mutableListOf<Int>()
    var allWordsIds: HashMap<Int, Note> = HashMap()
    var isGameActive = false
    var isGameInPause = false
    var isRoundFinished = false
    var isAllGameFinished = false
    var teams = mutableListOf<String>()
    var isCountWordsFinished = false
    var teamName1 = ""
    var teamName2 = ""
    var roundResults = mutableListOf<Int>()
    var countCrocoInRound = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.gameplay)

        teamName1 = intent.getStringExtra("teamName1").toString()
        teamName2 = intent.getStringExtra("teamName2").toString()
        if (teamName1 != null && teamName2 != null) {
            teams.add(teamName1)
            teams.add(teamName2)
        }

        val countOfWord = intent.getIntExtra("countWords", 50)
        val timeCounts = intent.getLongExtra("timerTime", 60)

        crocodil.setBackgroundResource(R.drawable.croco_run_animation)

        countWordsTablo.text = "Count words: $countOfWord"
        teamTablo.text = teamName1
        time = timeCounts

        val random = Random()
        while (allWordsIds.size < countOfWord) {
            val randomInt: Int = random.nextInt(db.notesCount - 1) + 1
            val note = db.getNote(randomInt.toLong())
            allWordsIds[randomInt] = note
        }

        val startAlertDialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("Hello!!!")
            .setMessage("First command: $teamName1")
            .setPositiveButton(android.R.string.yes,
                DialogInterface.OnClickListener { dialog, which ->
                    // Continue with delete operation
                })
            .create()

        startAlertDialog.show()

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        functionalityOfButtons(sharedPref)
    }

    @SuppressLint("SetTextI18n")
    fun functionalityOfButtons(sharedPref: SharedPreferences) = run {

        var isOddGame = true
        var playerBall1 = 0
        var playerBall2 = 0
        val width = Resources.getSystem().displayMetrics.widthPixels

        val timeFinishedAlertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            .setTitle("Round finished")
            .setPositiveButton(android.R.string.yes,
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    val intent = Intent(this, RoundActivity::class.java)
                    intent.putExtra("roundWords", roundWords.toIntArray())
                    startActivity(intent)
                }
            )

        val frameAnimation: AnimationDrawable = crocodil.getBackground() as AnimationDrawable

        val countDownTimer = object : SonicCountDownTimer(time * 1000, 1000) {

            override fun onTimerTick(timeRemaining: Long) {
                val minutes = (timeRemaining / 1000) / 60
                val seconds = (timeRemaining / 1000) % 60
                crocodil.x += (width / time - 0.5).toFloat()
                frameAnimation.start()
                timer.text = String.format("%d:%02d", minutes, seconds)
                isRoundFinished = false
                skipButton.text = "Skip"
            }

            override fun onTimerFinish() {
                isOddGame = isOddGame.not()
                if (!isAllGameFinished) {
                    val alert = timeFinishedAlertDialogBuilder
                        .setMessage(
                            "You can fix your words: \"Fix up\". \n The next team is ${
                                getGeneralTeam(
                                    isOddGame
                                )
                            }"
                        )
                        .create()
                    alert.show()
                }
                isGameActive = false
                isGameInPause = false
                play_button.setBackgroundResource(R.drawable.play_button)
                nonActiveGameState()
                frameAnimation.stop()
                isRoundFinished = true
                if (!isAllGameFinished){
                    skipButton.visibility = View.VISIBLE
                    skipButton.text = "Fix Up"
                    teamTablo.text = getGeneralTeam(isOddGame)
                }
                roundResults.add(countCrocoInRound)
                countCrocoInRound = 0
            }
        }

        book.setOnClickListener {
            val intent = Intent(this, BookActivity::class.java)
            startActivity(intent)
        }

        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent, 0)
//            time = intent.getLongExtra("tag1")
        }

        val randomIdFromChooseWord = Random()
        var currentId = 0

        play_button.setOnClickListener {
            if (!isGameActive) {
                activeGameState()
                crocodil.x = -800F
                currentId = setDataFromNote(randomIdFromChooseWord)
                countDownTimer.startCountDownTimer()
                roundWords = mutableListOf()
                isGameActive = isGameActive.not()
                play_button.setBackgroundResource(R.drawable.pause_button)
            } else if (isGameActive && isGameInPause) {
                activeGameState()
                countDownTimer.resumeCountDownTimer()
                play_button.setBackgroundResource(R.drawable.pause_button)
                isGameInPause = isGameInPause.not()
                frameAnimation.start()
            } else if (isGameActive && !isGameInPause) {
                nonActiveGameState()
                play_button.setBackgroundResource(R.drawable.play_button)
                countDownTimer.pauseCountDownTimer()
                isGameInPause = isGameInPause.not()
                frameAnimation.stop()
            }
        }

        skipButton.setOnClickListener {
            if (isGameActive) {
                currentId = setDataFromNote(randomIdFromChooseWord)
                roundWords.add(currentId)
            } else if (isRoundFinished) {
                val intent = Intent(this, RoundActivity::class.java)
                intent.putExtra("roundWords", roundWords.toIntArray())
                startActivity(intent)
            }
            R.id.settings
        }

        val countWordsFinishefAlertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            .setTitle("Words finished!!!")
            .setPositiveButton(android.R.string.yes,
                DialogInterface.OnClickListener { dialog, which ->
                    // Continue with delete operation
                })

        crocoButton.setOnClickListener {
            if (isGameActive) {
                roundWords.add(currentId)
                allWordsIds.remove(currentId)
                if (isOddGame) {
                    playerBall1++
                } else {
                    playerBall2++
                }
                countCrocoInRound++
                countWordsTablo.text = "Count words: ${allWordsIds.size}"
                if (allWordsIds.size != 0) {
                    println("allWordsIds.size ${allWordsIds.size}")
                    println("allWordsIds: $allWordsIds")
                    currentId = setDataFromNote(randomIdFromChooseWord)
                } else {
                    nonActiveGameState()
                    frameAnimation.stop()
                    isAllGameFinished = true
                    play_button.visibility = View.INVISIBLE
                    timer.visibility = View.INVISIBLE

                    countDownTimer.stopCountDownTimer()
                    crocoButton.visibility = View.VISIBLE
                    crocoButton.text = "Statistic"
                    val winnerTeam = if (playerBall1 > playerBall2){
                        teamName1
                    } else {
                        teamName2
                    }
                    main_word.text = "WIN: $winnerTeam"
                    tips_of_word.text = "Congratulation!"
                    val r = countWordsFinishefAlertDialog
                        .setMessage("Congratulations!!! $winnerTeam is win \n Result: $playerBall1 / $playerBall2")
                        .create()
                    r.show()
                }
            } else if (isAllGameFinished){
                val i = Intent(this, StatisticActivity::class.java)
                i.putExtra("roundResults", roundResults.toIntArray())
                i.putExtra("teamName1", teamName1)
                i.putExtra("teamName2", teamName2)
                i.putExtra("teamName1Res", playerBall1)
                i.putExtra("teamName2Res", playerBall2)

                startActivity(i)
            }
        }
    }

    private fun setDataFromNote(randomIdFromChooseWord: Random): Int {
        val keys: List<Int> = allWordsIds.keys.toList()
        val randomInt = randomIdFromChooseWord.nextInt(keys.size)
        val randomKey = keys[randomInt]
        val note: Note? = allWordsIds[randomKey]

        if (note != null) {
            main_word.text = note.word
            tips_of_word.text = note.description
        }
        return randomKey
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (data != null) {
            println("_______________________")
            println(data.getStringExtra("settings_tips"))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun activeGameState() {
        book.visibility = View.INVISIBLE
        settings.visibility = View.INVISIBLE
        crocoButton.visibility = View.VISIBLE
        skipButton.visibility = View.VISIBLE
        timer.visibility = View.VISIBLE
    }

    fun nonActiveGameState() {
        crocoButton.visibility = View.INVISIBLE
        skipButton.visibility = View.INVISIBLE
        book.visibility = View.VISIBLE
        settings.visibility = View.VISIBLE
        if (isGameInPause) {
            timer.visibility = View.GONE
        }
    }

    fun getGeneralTeam(isOddGame: Boolean): String {
        if (isOddGame) {
            return teams[0]
        }
        return teams[1]
    }
}
