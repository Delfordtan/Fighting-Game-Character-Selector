package com.productions.delford.temprandomhelper

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_choose_game.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.alert

class ChooseGame : AppCompatActivity() {


    var game: Game? = null
    var count = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_game)

        chooseGame()

        val chooseGameBtn = chooseGame

        chooseGameBtn.onClick {
            game = null
            chooseGame() }

    }


    fun chooseGame() {

        alert {

            title = "Choose a game:"
            customView {



                val linear = linearLayout {

                    var ggBtn = Button(context)
                    var bbBtn = Button(context)
                    var injustBtn = Button(context)
                    var sfvBtn = Button(context)

                    fun turnOffAllBtns() {
                        ggBtn.textColor = Color.BLACK
                        bbBtn.textColor = Color.BLACK
                        injustBtn.textColor = Color.BLACK
                        sfvBtn.textColor = Color.BLACK
                    }

                    ggBtn = button {
                        text = "Guilty Gear"
                        layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
                        onClick {
                            turnOffAllBtns()
                            textColor = Color.RED
                            game = Game.GG

                        }
                    }

                    bbBtn = button {
                        text = "Blazblue"
                        layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)

                        onClick {
                            turnOffAllBtns()
                            textColor = Color.RED
                            game = Game.BB
                        }

                    }

                    injustBtn = button {
                        text = "Injustice"
                        layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)

                        onClick {
                            turnOffAllBtns()
                            textColor = Color.RED
                            game = Game.Injust
                        }

                    }

                    sfvBtn = button {
                        text = "SFV"
                        layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)

                        onClick {
                            turnOffAllBtns()
                            textColor = Color.RED
                            game = Game.SFV
                        }


                    }

                }

                linear.gravity = android.view.Gravity.CENTER
                linear.orientation = LinearLayout.VERTICAL


            }

            positiveButton("Confirm"){

                if (game == null) {
                    alert("Please choose a game"){
                        positiveButton("OK"){}
                    }.show()
                } else {
                    selectnoofMatches(game!!, this@ChooseGame )

                }
            }
            negativeButton("Cancel"){
                game = null
            }

        }.show()
    }


    fun selectnoofMatches(game: Game, context: Context) {
        count = 1

        val characterCount = CharacterData(game).characterList().count()


        alert {
            customView {
                title = "Select no. of Matches"
                var textviewy = TextView(context)
                val linear = linearLayout{

                    button("-") {
                        textSize = 20.toFloat()
                        layoutParams = LinearLayout.LayoutParams(wrapContent, wrapContent, 0.1f)

                        onClick {
                            if (count > 1) {
                                count -= 1
                                textviewy.text = "$count vs $count"
                            }
                        }
                    }

                    textviewy = textView ("$count vs $count") {
                        textSize = 30.toFloat()
                        layoutParams = LinearLayout.LayoutParams(matchParent, matchParent, 0.7f)
                        gravity = android.view.Gravity.CENTER
                    }

                    button("+") {
                        textSize = 20.toFloat()
                        layoutParams = LinearLayout.LayoutParams(wrapContent, wrapContent, 0.1f)

                        onClick {
                            if (count < characterCount) {
                                count += 1
                                textviewy.text = "$count vs $count"
                            }
                        }

                    }

                }

                linear.gravity = android.view.Gravity.CENTER


            }
            positiveButton("Confirm"){
                startActivity<ChooseCharacters>("count" to count, "game" to game)
            }
            negativeButton("Cancel"){}
        }.show()
    }


}

enum class Game {
    GG, BB, Injust, SFV
}

class CharacterData (game: Game) {


    var game: Game = game

    fun characterList(): List<String>  {

        val list: List<String>
        when (game) {
            Game.GG -> list = mutableListOf("Sol Badguy", "Ky", "Millia", "Zato-1", "May", "Potemkin", "Chipp", "Venom", "Axl Low",
                    "I-No", "Faust", "Slayer", "Ramlethal", "Bedman", "Sin", "Elphelt", "Leo", "Johnny", "Jack-O", "Jam", "Raven",
                    "Haehyun", "Dizzy", "Baiken", "Answer").sorted()
            Game.BB -> list = mutableListOf("Amane", "Arakune", "Azrael", "Bang", "Bullet", "Carl", "Celica", "Es", "Hakumen",
                    "Hazama", "Hibiki", "Tager", "Izanami", "Izayoi", "Jin", "Jubei", "Kagura", "Kokonoe", "Litchi", "Mai", "Makoto",
                    "Naoto", "Nine", "Noel", "Platinum", "Rachel", "Ragna", "Relius", "Susanoo", "Taokaka", "Tsubaki", "Valkenhayn",
                    "Terumi", "Λ-No.11", "μ-No.12", "ν-No.13").sorted()
            Game.Injust -> list = mutableListOf("Superman", "Aquaman", "Atom", "Atrocitus", "Bane", "Batman", "B. Adam", "B. Manta",
                    "B. Canary", "B.Beetle", "Brainiac", "Captain C.", "Catwoman", "Cheetah", "Cyborg", "Darkseid", "Deadshot",
                    "Dr. Fate", "Enchantress", "Firestorm", "Flash", "Gorrila G.", "Green Arrow", "G. Lantern", "Harley Quinn",
                    "Hellboy", "Joker", "Poison Ivy", "Raiden", "Red Hood", "Robin", "Scarecrow", "Starfire", "Sub-Zero", "Supergirl",
                    "Swamp Thing", "Wonder Woman", "Donatello", "Michaelangelo", "Raphael", "Leonardo").sorted()
            Game.SFV -> list = mutableListOf("Ryu", "Chunli", "Nash", "Bison", "Cammy", "Birdie", "Ken", "Nacalli", "Vega", "R.Mika",
                    "Rashid", "Karin", "Zangief", "Laura", "Dhalsim", "F.A.N.G.", "Alex", "Guile", "Ibuki", "Balrog", "Juri", "Urien",
                    "Akuma", "Kolin", "Ed", "Abigail", "Menat", "Zeku", "Sakura", "Blanka", "Falke", "Cody", "G", "Sagat").sorted()
        }

        return list
    }


}
