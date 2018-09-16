package com.productions.delford.temprandomhelper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager

import com.github.ajalt.flexadapter.FlexAdapter
import com.github.ajalt.flexadapter.register
import kotlinx.android.synthetic.main.activity_choose_characters.*
import kotlinx.android.synthetic.main.choose_character_cell.view.*
import kotlinx.android.synthetic.main.chosen_character_cell.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

/**
 * For choosing characters
 */
class ChooseCharacters : AppCompatActivity() {

    /**
     * counter for no. of characters to be selected
     */
    private var count: Int = 0
    var game: Game? = null
    private var p1Characters = mutableListOf<Character>()
    private var p2Characters = mutableListOf<Character>()
    var currentPlayer = Player.P1
    lateinit var characterList: List<String>
    lateinit var chosenCharacters: BooleanArray




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_characters)

        count = intent.extras.getInt("count")
        game = intent.extras.get("game") as Game
        characterList = CharacterData(game!!).characterList()
        chosenCharacters = BooleanArray(characterList.count())


        title = "Choose your characters: P1"

        val chooseAdapter = FlexAdapter<Character>()
        val chosenListAdapter = FlexAdapter<Character>()

        // for the character selection recyclerView


        chooseCharacterList.adapter = chooseAdapter

        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = chooseAdapter.spanSizeLookup
        chooseCharacterList.layoutManager = layoutManager

        with(chooseAdapter) {
            register<Character>(R.layout.choose_character_cell, span = 1) { it, v, _ ->
                val name = it.name
                val character = it

                v.choose_character_cell_character_name.text = name
                v.choose_character_cell_character_name.onClick {

                    if (chosenCharacters[character.index]) {

                        removeCharacter(chosenListAdapter, character)
                        chosenCharacters[character.index] = false


                    } else {

                        if (chosenListAdapter.itemCount < count) {
                            addCharacter(chosenListAdapter, character)
                            chosenCharacters[character.index] = true
                        }

                    }

                }
            }

        }
        chooseAdapter.items.addAll(generateCharacterList())


        // for the selectedCharacterView

        chosenCharacterList.adapter = chosenListAdapter

        val chosenLayoutManager = GridLayoutManager(this, 2)
        chosenLayoutManager.spanSizeLookup = chosenListAdapter.spanSizeLookup
        chosenCharacterList.layoutManager = chosenLayoutManager

        with(chosenListAdapter) {
            register<Character>(R.layout.chosen_character_cell, span = 1) { it, v, _ ->
                val name = it.name
                val character = it
                v.chosenCharacterCell.text = name
                v.chosenCharacterCell.onClick {

                    removeCharacter(chosenListAdapter, character)
                    chosenCharacters[character.index] = false


                }
            }

        }

        resetBtn.onClick {
            chosenListAdapter.items.clear()
            chosenCharacters = BooleanArray(characterList.count())

        }

        confirmBtn.onClick {

            if (chosenListAdapter.itemCount == count) {

                    when (currentPlayer) {
                        Player.P1 -> {


                            alert("Confirm character selection?"){
                                positiveButton("Confirm") {
                                    p1Characters = chosenListAdapter.items.toMutableList()
                                    chosenListAdapter.items.clear()
                                    chosenCharacters = BooleanArray(characterList.count())
                                    this@ChooseCharacters.title = "Choose your characters: P2"
                                    currentPlayer = Player.P2
                                }
                                negativeButton("Cancel"){}
                            }.show()

                        }
                        Player.P2 -> {

                            alert("Confirm character selection?") {
                                positiveButton("Confirm") {
                                    p2Characters = chosenListAdapter.items.toMutableList()
                                    startActivity<MatchScreen>("p1" to p1Characters, "p2" to p2Characters)
                                }
                                negativeButton("Cancel"){}
                            }.show()

                        }

                }


            } else {
                alert("Please select $count characters") {
                    okButton {  }
                }.show()
            }

        }


    }



    fun addCharacter (adapter: FlexAdapter<Character>, character: Character) {

            adapter.items.add(character)


    }

    fun removeCharacter (adapter: FlexAdapter<Character>, character: Character) {

        adapter.items.remove(character)
    }

    private fun generateCharacterList(): MutableList<Character> {

        val list = mutableListOf<Character>()

        characterList.forEachIndexed { index, character ->
            list.add(Character(character, index))

        }

        return list


    }


}

enum class Player {
    P1, P2
}
