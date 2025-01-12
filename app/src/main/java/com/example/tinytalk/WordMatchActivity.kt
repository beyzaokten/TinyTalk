package com.example.tinytalk

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tinytalk.model.Animal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import android.media.MediaPlayer

class WordMatchActivity : AppCompatActivity() {
    private lateinit var words: List<Animal>
    private lateinit var wordImageView: ImageView
    private lateinit var wordTextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var checkButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var backButton: ImageView

    private var currentIndex = 0
    private lateinit var correctSound: MediaPlayer
    private lateinit var wrongSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_match)

        wordImageView = findViewById(R.id.wordImageView)
        wordTextView = findViewById(R.id.wordTextView)
        answerEditText = findViewById(R.id.answerEditText)
        checkButton = findViewById(R.id.checkButton)
        resultTextView = findViewById(R.id.resultTextView)
        backButton = findViewById(R.id.backButton)

        // JSON verilerini yükle
        words = loadWordsFromJson()

        // Doğru ve yanlış müzik dosyalarını yükle
        correctSound = MediaPlayer.create(this, R.raw.correct)
        wrongSound = MediaPlayer.create(this, R.raw.wrong)

        // İlk kelimeyi göster
        displayWord()

        // Kontrol butonu
        checkButton.setOnClickListener {
            checkAnswer()
        }

        // Geri butonu
        backButton.setOnClickListener {
            val intent = Intent(this, GameMenuActivity::class.java)
            startActivity(intent)
            finish()  // Bu aktiviteyi bitir
        }
    }

    private fun loadWordsFromJson(): List<Animal> {
        val inputStream = assets.open("animals.json")
        val reader = InputStreamReader(inputStream)
        val wordType = object : TypeToken<List<Animal>>() {}.type
        return Gson().fromJson(reader, wordType)
    }

    private fun displayWord() {
        val currentWord = words[currentIndex]
        val resId = resources.getIdentifier(
            currentWord.imageName.replace(".png", ""), "drawable", packageName
        )
        wordImageView.setImageResource(resId)

        // İlk harfi göster ve geri kalan harfler için boşluklar ekle
        val firstLetter = currentWord.englishName.first()
        val wordLength = currentWord.englishName.length
        var displayText = "$firstLetter"

        for (i in 1 until wordLength) {
            displayText += " _"
        }

        wordTextView.text = displayText
    }

    private fun checkAnswer() {
        val userAnswer = answerEditText.text.toString().trim()
        val correctAnswer = words[currentIndex].englishName

        if (userAnswer.equals(correctAnswer, ignoreCase = true)) {
            resultTextView.text = "Doğru!"
            correctSound.start()  // Doğru müziği çal
            currentIndex = (currentIndex + 1) % words.size
            displayWord()
            answerEditText.text.clear()
        } else {
            resultTextView.text = "Tekrar Dene!"
            wrongSound.start()  // Yanlış müziği çal
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        correctSound.release()
        wrongSound.release()
    }
}
