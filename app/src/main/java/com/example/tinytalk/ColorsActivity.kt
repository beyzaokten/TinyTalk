package com.example.tinytalk

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tinytalk.model.Color
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.util.Locale

class ColorsActivity : AppCompatActivity() {
    private lateinit var colors: List<Color>
    private lateinit var tts: TextToSpeech
    private var currentIndex = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colors)

        // Text-to-Speech
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
            }
        }

        colors = loadColorsFromJson()

        val colorImage = findViewById<ImageView>(R.id.colorsImage)
        val englishName = findViewById<TextView>(R.id.englishName)
        val turkishName = findViewById<TextView>(R.id.turkishName)
        val speakButton = findViewById<ImageView>(R.id.speakButton)

        displayColor(colorImage, englishName, turkishName)

        // Seslendirme butonu
        speakButton.setOnClickListener {
            val textToSpeak = colors[currentIndex].englishName
            tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
        }

        // Sağ ve sol ok butonları
        findViewById<ImageView>(R.id.nextButton).setOnClickListener {
            currentIndex = (currentIndex + 1) % colors.size
            displayColor(colorImage, englishName, turkishName)
        }

        findViewById<ImageView>(R.id.prevButton).setOnClickListener {
            currentIndex = if (currentIndex - 1 < 0) colors.size - 1 else currentIndex - 1
            displayColor(colorImage, englishName, turkishName)
        }

        // Geri butonu
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadColorsFromJson(): List<Color> {
        val inputStream = assets.open("colors.json")
        val reader = InputStreamReader(inputStream)
        val colorType = object : TypeToken<List<Color>>() {}.type
        val colors = Gson().fromJson<List<Color>>(reader, colorType)

        if (colors.isNullOrEmpty()) {
            throw RuntimeException("Color list is empty or null!")
        }
        return colors
    }

    private fun displayColor(
        imageView: ImageView,
        englishNameView: TextView,
        turkishNameView: TextView
    ) {
        val color = colors[currentIndex]

        val resId = resources.getIdentifier(
            color.imageName.replace(".png", ""),
            "drawable",
            packageName
        )
        imageView.setImageResource(resId)

        englishNameView.text = color.englishName
        turkishNameView.text = color.turkishName
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}
