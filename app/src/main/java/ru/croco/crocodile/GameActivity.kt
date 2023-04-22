package ru.croco.crocodile

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.orbitalsonic.sonictimer.SonicCountDownTimer
import kotlinx.android.synthetic.main.gameplay.*
import kotlinx.android.synthetic.main.start.book
import kotlinx.android.synthetic.main.start.settings
import java.util.*


class GameActivity : AppCompatActivity() {
    /**
     * Игровое поле
     */
    val db = DatabaseHelper(this)
    var time: Long = 10

    var roundWordsChecked = mutableSetOf<Int>()
    var roundWordsUnchecked = mutableSetOf<Int>()
    var allWordsIds: HashMap<Int, Note> = HashMap()
    var isGameActive = false
    var isGameInPause = false
    var isRoundFinished = false
    var isAllGameFinished = false
    var teams = mutableListOf<String>()
    var teamName1 = ""
    var teamName2 = ""
    var roundResults = mutableListOf<Int>()
    var countCrocoInRound = 0
    var isOddGame = true
    var isAnimationCroco = true


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        val sharedPref = this.getSharedPreferences(
            "ru.kongosha.friends", MODE_PRIVATE
        )
        val isDatabaseInitializer = sharedPref
            .getBoolean("ru.kongosha.friends.IsDatabaseInitialized", false)
        if (!isDatabaseInitializer) {
            MainActivity.loadDatabase(db, this)
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.gameplay)

        getTeamNames()

        val countOfWord = intent.getIntExtra("countWords", 5)
        val timeCounts = intent.getLongExtra("timerTime", 10)

        crocodil.setBackgroundResource(R.drawable.croco_run_animation)

        countWordsTablo.text = "Count words: $countOfWord"
        teamTablo.text = teamName1
        time = timeCounts
        allWordsIds = getWordsFromDB(countOfWord, db)

        val startAlertDialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("Turn order")
            .setMessage("Team '$teamName1'! It is your round. Are you ready?")
            .setPositiveButton(android.R.string.yes,
                DialogInterface.OnClickListener { dialog, which ->
                    // Continue with delete operation
                })
            .create()

        startAlertDialog.show()

        functionalityOfButtons()
    }

    fun getTeamNames() {
        teamName1 = intent.getStringExtra("teamName1").toString()
        teamName2 = intent.getStringExtra("teamName2").toString()
        if (teamName1 != null && teamName2 != null) {
            teams.add(teamName1)
            teams.add(teamName2)
        }
    }

    fun timer(): SonicCountDownTimer {
        val width = Resources.getSystem().displayMetrics.widthPixels

        val timeFinishedAlertDialogBuilder = getAlertForNextTeam()
        val frameAnimation: AnimationDrawable = crocodil.getBackground() as AnimationDrawable

        return object : SonicCountDownTimer(time * 1000, 1000) {

            override fun onTimerTick(timeRemaining: Long) {
                val minutes = (timeRemaining / 1000) / 60
                val seconds = (timeRemaining / 1000) % 60
                if(isAnimationCroco) {
                    crocodil.x += (width / time - 0.5).toFloat()
                    frameAnimation.start()
                }
                timer.text = String.format("%d:%02d", minutes, seconds)
                isRoundFinished = false
                skipButton.text = "Skip"
            }

            @SuppressLint("SetTextI18n")
            override fun onTimerFinish() {
                isOddGame = isOddGame.not()
                if (!isAllGameFinished) {
                    val alert = timeFinishedAlertDialogBuilder
                        .setMessage(
                            "You can fix your words: \"Fix up\". \n The next team is ${
                                getGeneralTeam(
                                    isOddGame, teams
                                )
                            }"
                        )
                        .create()
                    if (!isFinishing) {
                        alert.show()
                    }
                }
                isGameActive = false
                isGameInPause = false
                play_button.setBackgroundResource(R.drawable.play_button)
                nonActiveGameState()
                if (isAnimationCroco) {
                    frameAnimation.stop()
                }
                isRoundFinished = true
                if (!isAllGameFinished) {
                    skipButton.visibility = View.VISIBLE
                    skipButton.text = "Fix Up"
                    teamTablo.text = getGeneralTeam(isOddGame, teams)
                }
                roundResults.add(countCrocoInRound)
                countCrocoInRound = 0
                main_word.text = "STOP GAME"
                tips_of_word.text = "Time finished for ${getGeneralTeam(isOddGame.not(), teams)}."
            }
        }

    }

    private fun getAlertForNextTeam(): AlertDialog.Builder {
        return AlertDialog.Builder(this)
            .setTitle("Round finished")
            .setPositiveButton(android.R.string.yes,
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    val intent = Intent(this, RoundActivity::class.java)
                    intent.putExtra("roundWordsChecked", roundWordsChecked.toIntArray())
                    intent.putExtra("roundWordsUnchecked", roundWordsUnchecked.toIntArray())
                    startActivityForResult(intent, 10)
                }
            )
    }


    private fun getAlertForFinishWords(): AlertDialog.Builder {
        return AlertDialog.Builder(this)
            .setTitle("Words finished            !!!")
            .setPositiveButton(android.R.string.yes,
                DialogInterface.OnClickListener { dialog, which ->
                    // Continue with delete operation
                })
    }

    @SuppressLint("SetTextI18n")
    fun functionalityOfButtons() = run {
        val countDownTimer = timer()
        var playerBall1 = 0
        var playerBall2 = 0

        val randomIdFromChooseWord = Random()
        var currentWordId = 0
        val frameAnimation: AnimationDrawable = crocodil.getBackground() as AnimationDrawable

        book.setOnClickListener {
            val intent = Intent(this, BookActivity::class.java)
            startActivity(intent)
        }
        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent, 0)
        }
        play_button.setOnClickListener {
            if (!isGameActive) {
                val sharedPref2 = PreferenceManager.getDefaultSharedPreferences(this)
                isAnimationCroco = sharedPref2.getBoolean("animation_croco", true)
                if (sharedPref2.getBoolean("example_switch", true)) {
                    tips_of_word.visibility = View.VISIBLE
                } else {
                    tips_of_word.visibility = View.INVISIBLE
                }

                currentWordId = setDataFromNote(randomIdFromChooseWord, allWordsIds)
                activeGameState()
                if (isAnimationCroco) {
                    crocodil.x = -800F
                }
                countDownTimer.startCountDownTimer()
                roundWordsChecked = mutableSetOf()
                roundWordsUnchecked = mutableSetOf()
                isGameActive = isGameActive.not()
                play_button.setBackgroundResource(R.drawable.pause_button)


            } else if (isGameActive && isGameInPause) {
                activeGameState()
                countDownTimer.resumeCountDownTimer()
                play_button.setBackgroundResource(R.drawable.pause_button)
                isGameInPause = isGameInPause.not()
                if (isAnimationCroco) {
                    frameAnimation.start()
                }
                val sharedPref2 = PreferenceManager.getDefaultSharedPreferences(this)
                println("sharedPref: ${sharedPref2.getBoolean("example_switch", true)}")
                if (sharedPref2.getBoolean("example_switch", true)) {
                    tips_of_word.visibility = View.VISIBLE
                } else {
                    tips_of_word.visibility = View.INVISIBLE
                }
            } else if (isGameActive && !isGameInPause) {
                nonActiveGameState()
                play_button.setBackgroundResource(R.drawable.play_button)
                countDownTimer.pauseCountDownTimer()
                isGameInPause = isGameInPause.not()
                main_word.visibility = View.INVISIBLE
                tips_of_word.visibility = View.INVISIBLE

                if (isAnimationCroco) {
                    frameAnimation.stop()
                }
            }
        }

        skipButton.setOnClickListener {
            if (isGameActive && !isGameInPause) {
                roundWordsUnchecked.add(currentWordId)
                currentWordId = setDataFromNote(randomIdFromChooseWord, allWordsIds)
            } else if (isRoundFinished) {
                val intent = Intent(this, RoundActivity::class.java)
                intent.putExtra("roundWordsChecked", roundWordsChecked.toIntArray())
                intent.putExtra("roundWordsUnchecked", roundWordsUnchecked.toIntArray())
                startActivityForResult(intent, 10)
            }
        }

        crocoButton.setOnClickListener {
            if (isGameActive && !isGameInPause) {
                roundWordsChecked.add(currentWordId)
                allWordsIds.remove(currentWordId)
                if (isOddGame) {
                    playerBall1++
                } else {
                    playerBall2++
                }
                countCrocoInRound++
                countWordsTablo.text = "Count words: ${allWordsIds.size}"
                if (allWordsIds.size != 0) {
                    currentWordId = setDataFromNote(randomIdFromChooseWord, allWordsIds)
                } else {
                    nonActiveGameState()
                    if (isAnimationCroco) {
                        frameAnimation.stop()
                    }
                    isAllGameFinished = true
                    play_button.visibility = View.INVISIBLE
                    timer.visibility = View.INVISIBLE

                    countDownTimer.stopCountDownTimer()
                    crocoButton.visibility = View.VISIBLE
                    crocoButton.text = "Statistic"
                    val winnerTeam = if (playerBall1 > playerBall2) {
                        teamName1
                    } else {
                        teamName2
                    }
                    main_word.text = "WIN: $winnerTeam"
                    tips_of_word.text = "Congratulation!"
                    val countWordsFinishefAlertDialog = getAlertForFinishWords()
                    val r = countWordsFinishefAlertDialog
                        .setMessage("Congratulations!!! $winnerTeam is win \n Result: $playerBall1 / $playerBall2")
                        .create()
                    if (!isFinishing) {
                        r.show()
                    }
                }
            } else if (isAllGameFinished) {
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

    private fun setDataFromNote(
        randomIdFromChooseWord: Random,
        allWordsIds: HashMap<Int, Note>
    ): Int {
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


    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (resultCode == 10) {
                roundWordsUnchecked = if (data.getIntArrayExtra("uncheckedWords")?.size == 0) {
                    mutableSetOf()
                } else {
                    data.getIntArrayExtra("uncheckedWords")?.toSet() as MutableSet<Int>
                }
                roundWordsChecked = if (data.getIntArrayExtra("checkedWords")?.size == 0) {
                    mutableSetOf()
                } else {
                    data.getIntArrayExtra("checkedWords")?.toSet() as MutableSet<Int>
                }
                if (roundWordsUnchecked != null && roundWordsChecked != null) {
                    for (idx in roundWordsUnchecked) {
                        if (!allWordsIds.containsKey(idx)) {
                            val note = db.getNote(idx.toLong())
                            allWordsIds[idx] = note
                        }
                    }
                    for (idx in roundWordsChecked) {
                        if (allWordsIds.containsKey(idx)) {
                            allWordsIds.remove(idx)
                        }
                    }
                    countWordsTablo.text = "Count words: ${allWordsIds.size}"
                }
            }
        }
    }

    fun activeGameState() {
        book.visibility = View.INVISIBLE
        settings.visibility = View.INVISIBLE
        crocoButton.visibility = View.VISIBLE
        skipButton.visibility = View.VISIBLE
        timer.visibility = View.VISIBLE
        main_word.visibility = View.VISIBLE
//        tips_of_word.visibility = View.VISIBLE
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

    companion object {
        fun getGeneralTeam(isOddGame: Boolean, teams: List<String>): String {
            if (isOddGame) {
                return teams[0]
            }
            return teams[1]
        }

        fun getWordsFromDB(n: Int, db: DatabaseHelper): HashMap<Int, Note> {
            val res: HashMap<Int, Note> = HashMap()
            val random = Random()
            while (res.size < n) {
                val randomInt: Int = random.nextInt(db.notesCount - 1) + 1
                val note = db.getNote(randomInt.toLong())
                res[randomInt] = note
            }
            return res
        }
    }
}
