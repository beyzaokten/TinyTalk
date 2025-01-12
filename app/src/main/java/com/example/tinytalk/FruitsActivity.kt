package com.example.tinytalk

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tinytalk.model.Fruit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.util.Locale

class FruitsActivity : AppCompatActivity() {
    private lateinit var fruits: List<Fruit>
    private lateinit var tts: TextToSpeech
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruits)

        // Text-to-Speech
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
            }
        }

        fruits = loadFruitsFromJson()

        val fruitImage = findViewById<ImageView>(R.id.fruitImage)
        val englishName = findViewById<TextView>(R.id.englishName)
        val turkishName = findViewById<TextView>(R.id.turkishName)
        val speakButton = findViewById<ImageView>(R.id.speakButton)

        displayFruit(fruitImage, englishName, turkishName)

        // Seslendirme butonu
        speakButton.setOnClickListener {
            val textToSpeak = fruits[currentIndex].englishName
            tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
        }

        // Sağ ve sol ok butonları
        findViewById<ImageView>(R.id.nextButton).setOnClickListener {
            currentIndex = (currentIndex + 1) % fruits.size
            displayFruit(fruitImage, englishName, turkishName)
        }

        findViewById<ImageView>(R.id.prevButton).setOnClickListener {
            currentIndex = if (currentIndex - 1 < 0) fruits.size - 1 else currentIndex - 1
            displayFruit(fruitImage, englishName, turkishName)
        }

        // Geri butonu
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadFruitsFromJson(): List<Fruit> {
        val inputStream = assets.open("fruits.json")
        val reader = InputStreamReader(inputStream)
        val fruitType = object : TypeToken<List<Fruit>>() {}.type
        val fruits = Gson().fromJson<List<Fruit>>(reader, fruitType)

        if (fruits.isNullOrEmpty()) {
            throw RuntimeException("Fruit list is empty or null!")
        }
        return fruits
    }

    private fun displayFruit(
        imageView: ImageView,
        englishNameView: TextView,
        turkishNameView: TextView
    ) {
        val fruit = fruits[currentIndex]

        val resId = resources.getIdentifier(
            fruit.imageName.replace(".png", ""),
            "drawable",
            packageName
        )
        imageView.setImageResource(resId)

        englishNameView.text = fruit.englishName
        turkishNameView.text = fruit.turkishName
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}
