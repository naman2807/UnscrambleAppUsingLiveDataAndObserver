package com.example.android.unscramble.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val _score = MutableLiveData(0)
    val score : LiveData<Int>  get() = _score

    private val _currentWordCount  = MutableLiveData(0)
    val currentWordCount: LiveData<Int> get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambleWord: LiveData<String> get() = _currentScrambledWord

    private var usedWordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    private var _count = 0
    val count: Int get() = _count

    init {
        getNextWord()
    }

    /*
     * Updates currentWord and currentScrambledWord with the next word.
     */
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val scrambledWord = currentWord.toCharArray()
        scrambledWord.shuffle()
        if (usedWordsList.contains(currentWord)) {
            getNextWord()
        } else {
            while (scrambledWord.toString().equals(currentWord, false)) {
                scrambledWord.shuffle()
            }
            _currentScrambledWord.value = String(scrambledWord)
           _currentWordCount.value = (_currentWordCount.value)?.inc()
            usedWordsList.add(currentWord)
        }
    }

    private fun increaseScore(){
       _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect(playerWord : String) : Boolean{
        if(playerWord.equals(currentWord)){
            increaseScore()
           return true
        }else{
            return false
        }
    }
    /*
     * Returns true if the current word count is less than MAX_NO_OF_WORDS.
     * Updates the next word.
     */
    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else {
            false
        }
    }

    /*
     * Re-initializes the game data to restart the game.
     */
    fun reinitializeData(){
        _score.value = 0
        _currentWordCount.value = 0
        usedWordsList.clear()
        getNextWord()
    }
}