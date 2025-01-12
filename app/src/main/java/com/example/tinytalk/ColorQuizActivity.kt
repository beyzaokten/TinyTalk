package com.example.tinytalk

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tinytalk.model.Color
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import kotlin.random.Random

class ColorQuizActivity : AppCompatActivity() {
    private lateinit var colors: List<Color>
    private lateinit var questionText: TextView
    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var correctAnswer: String
    private lateinit var correctSound: MediaPlayer
    private lateinit var wrongSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_quiz)

        // UI bileşenlerini tanımlama
        questionText = findViewById(R.id.questionText)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        val backButton: ImageView = findViewById(R.id.backButton)

        // Geri butonuyla GameMenuActivity'ye dönme
        backButton.setOnClickListener {
            finish()
        }

        colors = loadColorsFromJson()

        correctSound = MediaPlayer.create(this, R.raw.correct)
        wrongSound = MediaPlayer.create(this, R.raw.wrong)

        loadNewQuestion()

        // Şıklar için tıklama olayları
        option1.setOnClickListener { checkAnswer(option1, option1.text.toString()) }
        option2.setOnClickListener { checkAnswer(option2, option2.text.toString()) }
        option3.setOnClickListener { checkAnswer(option3, option3.text.toString()) }
    }

    private fun loadColorsFromJson(): List<Color> {
        val inputStream = assets.open("colors.json")
        val reader = InputStreamReader(inputStream)
        val colorType = object : TypeToken<List<Color>>() {}.type
        return Gson().fromJson<List<Color>>(reader, colorType)
    }

    private fun loadNewQuestion() {
        // Rastgele bir renk seç
        val randomIndex = Random.nextInt(colors.size)
        val questionColor = colors[randomIndex]
        correctAnswer = questionColor.turkishName // Doğru cevap

        // Soru metnini ayarla
        questionText.text = "Which color is ${questionColor.englishName}?"

        // Şıkları oluştur
        val options = mutableListOf(questionColor.turkishName) // Doğru cevap ekleniyor
        while (options.size < 3) {
            val option = colors[Random.nextInt(colors.size)].turkishName
            if (option !in options) options.add(option) // Aynı cevap eklenmez
        }

        // Şıkları karıştır
        options.shuffle()

        // Şıkları butonlara yerleştir
        option1.text = options[0]
        option2.text = options[1]
        option3.text = options[2]

        // Butonları görünür yap
        resetButtonVisibility()
    }

    private fun checkAnswer(button: Button, selectedAnswer: String) {
        if (selectedAnswer == correctAnswer) {
            Toast.makeText(this, "Doğru!", Toast.LENGTH_SHORT).show()
            correctSound.start() // Doğru müziği çal
            loadNewQuestion() // Yeni soru yükle
        } else {
            Toast.makeText(this, "Yanlış! Tekrar dene.", Toast.LENGTH_SHORT).show()
            wrongSound.start() // Yanlış müziği çal
            button.visibility = View.INVISIBLE // Yanlış şık görünmez olur
        }
    }

    private fun resetButtonVisibility() {
        // Butonların tekrar görünür olmasını sağlar
        option1.visibility = View.VISIBLE
        option2.visibility = View.VISIBLE
        option3.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        correctSound.release()
        wrongSound.release()
    }
}
