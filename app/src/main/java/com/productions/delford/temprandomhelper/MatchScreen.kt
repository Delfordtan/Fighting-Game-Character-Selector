package com.productions.delford.temprandomhelper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.github.ajalt.flexadapter.FlexAdapter
import com.github.ajalt.flexadapter.register
import kotlinx.android.synthetic.main.activity_match_screen2.*
import kotlinx.android.synthetic.main.match_cell.*
import kotlinx.android.synthetic.main.match_cell.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import java.util.*

class MatchScreen : AppCompatActivity() {

    var p1Characters = mutableListOf<Character>()
    var p2Characters = mutableListOf<Character>()
    var randomisedRounds = mutableListOf<Match>()
    var currentRound = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_screen2)

        title = "Match Screen"

        p1Characters = intent.extras.get("p1") as MutableList<Character>
        p2Characters = intent.extras.get("p2") as MutableList<Character>

        randomisedRounds = randomiseMatches(p1Characters, p2Characters)

        //Set up the first match screen
        loadRoundData(currentRound)




        // for setting up the matchRecyclerView
        val matchAdapter = FlexAdapter<Match>()

        matchRecyclerView.adapter = matchAdapter

        val layoutManager = GridLayoutManager(this, 1)
        layoutManager.spanSizeLookup = matchAdapter.spanSizeLookup
        matchRecyclerView.layoutManager = layoutManager

        with(matchAdapter) {
            register<Match>(R.layout.match_cell, span = 1) { it, v, position ->


                v.p1Character.text = it.p1Character.name
                v.p2Character.text = it.p2Character.name
                v.matchNumber.text = "${position + 1}."


                fun checkMatchWinner(): String {
                    when {
                        it.p1Score > it.p2Score -> return "P1"
                        it.p1Score < it.p2Score -> return "P2"
                        it.p1Score == it.p2Score -> return "Draw"
                        else -> return ""

                    }


                }

                when (it.winState) {
                    Match.WinState.Pending -> v.score.text = ""
                    else -> v.score.text = checkMatchWinner()
                }


            }
        }
        matchAdapter.items.addAll(randomisedRounds)


        //buttons

        p1RoundScore.onClick {
            p1RoundScore.text = (p1RoundScore.text.toString().toInt() + 1).toString()

        }

        p2RoundScore.onClick {
            p2RoundScore.text = (p2RoundScore.text.toString().toInt() + 1).toString()
        }

        reset.onClick {
            p1RoundScore.text = "0"
            p2RoundScore.text = "0"
        }

        confirm.onClick {


            randomisedRounds[currentRound].p1Score = p1RoundScore.text.toString().toInt()
            randomisedRounds[currentRound].p2Score = p2RoundScore.text.toString().toInt()
            randomisedRounds[currentRound].checkWinState()
            matchAdapter.notifyItemObjectChanged(randomisedRounds[currentRound])

            updateRoundScore()

            if ((currentRound + 1 ) >= randomisedRounds.count()) {

                val p1Score = p1TotalScore.text.toString().toInt()
                val p2Score = p2TotalScore.text.toString().toInt()

                alert {
                    message = checkWinner(p1Score, p2Score)
                    okButton {  }
                }.show()

            } else {
                currentRound += 1
                loadRoundData(currentRound)
            }
        }

        endMatchBtn.onClick {
            startActivity<ChooseGame>()
        }


    }

    fun updateRoundScore () {
        p1TotalScore.text = randomisedRounds.count { it.winState == Match.WinState.P1Win }.toString()
        p2TotalScore.text = randomisedRounds.count { it.winState == Match.WinState.P2Win }.toString()


    }

    fun checkWinner (p1Score: Int, p2Score: Int): String {


        when  {
            p1Score > p2Score -> return "Winner: P1!"
            p1Score < p2Score -> return "Winner: P2!"
            p1Score == p2Score -> return "Draw!"
            else -> return ""
        }

    }




    fun loadRoundData(round:Int) {

        val roundData = randomisedRounds[round]

        p1CharacterName.text = roundData.p1Character.name
        p2CharacterName.text = roundData.p2Character.name
        p1RoundScore.text = roundData.p1Score.toString()
        p2RoundScore.text = roundData.p2Score.toString()

        updateRoundScore()

    }

    fun randomiseMatches(p1characters: MutableList<Character>, p2characters: MutableList<Character>): MutableList<Match> {

        val p1CharactersList = p1characters
        val p2CharactersList = p2characters
        val matchArray = mutableListOf<Match>()

        while (p1CharactersList.isNotEmpty()) {

            Log.d("P1Ch:", "${p1CharactersList.count()}")
            Log.d("P2Ch:", "${p2CharactersList.count()}")
            val p1Selected = (0 until p1CharactersList.count()).random()
            val p2Selected = (0 until p2CharactersList.count()).random()

            Log.d("P1S:", "$p1Selected")
            Log.d("P2S:", "$p2Selected")

            matchArray.add(Match(p1CharactersList[p1Selected], p2CharactersList[p2Selected]))

            p1CharactersList.removeAt(p1Selected)
            p2CharactersList.removeAt(p2Selected)
        }

        return matchArray

    }
}


fun ClosedRange<Int>.random() =
        Random().nextInt((endInclusive + 1) - start) +  start




