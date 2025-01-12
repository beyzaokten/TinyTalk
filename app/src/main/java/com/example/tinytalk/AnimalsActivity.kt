package com.example.tinytalk

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tinytalk.model.Animal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.util.Locale

class AnimalsActivity : AppCompatActivity() {
    private lateinit var animals: List<Animal>
    private lateinit var tts: TextToSpeech
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animals)

        // Text-to-Speech
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
            }
        }

        animals = loadAnimalsFromJson()

        val animalImage = findViewById<ImageView>(R.id.animalImage)
        val englishName = findViewById<TextView>(R.id.englishName)
        val turkishName = findViewById<TextView>(R.id.turkishName)
        val speakButton = findViewById<ImageView>(R.id.speakButton)

        displayAnimal(animalImage, englishName, turkishName)

        // Seslendirme butonu
        speakButton.setOnClickListener {
            val textToSpeak = animals[currentIndex].englishName
            tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
        }

        // Sağ ve sol ok butonları
        findViewById<ImageView>(R.id.nextButton).setOnClickListener {
            currentIndex = (currentIndex + 1) % animals.size
            displayAnimal(animalImage, englishName, turkishName)
        }

        findViewById<ImageView>(R.id.prevButton).setOnClickListener {
            currentIndex = if (currentIndex - 1 < 0) animals.size - 1 else currentIndex - 1
            displayAnimal(animalImage, englishName, turkishName)
        }

        // Geri butonu
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadAnimalsFromJson(): List<Animal> {
        val inputStream = assets.open("animals.json")
        val reader = InputStreamReader(inputStream)
        val animalType = object : TypeToken<List<Animal>>() {}.type
        val animals = Gson().fromJson<List<Animal>>(reader, animalType)

        if (animals.isNullOrEmpty()) {
            throw RuntimeException("Animal list is empty or null!")
        }
        return animals
    }

    private fun displayAnimal(
        imageView: ImageView,
        englishNameView: TextView,
        turkishNameView: TextView
    ) {
        val animal = animals[currentIndex]

        val resId = resources.getIdentifier(
            animal.imageName.replace(".png", ""),
            "drawable",
            packageName
        )
        imageView.setImageResource(resId)

        englishNameView.text = animal.englishName
        turkishNameView.text = animal.turkishName
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}
