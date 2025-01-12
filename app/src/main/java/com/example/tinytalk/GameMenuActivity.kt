package com.example.tinytalk

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout

class GameMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_menu)

        // Geri butonu
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Word Match oyunu tıklanabilirliği
        val wordMatchGame: ImageView = findViewById(R.id.wordMatchGame)
        wordMatchGame.setOnClickListener {
            val intent = Intent(this, WordMatchActivity::class.java)
            startActivity(intent)
        }

        // Color Quiz oyunu tıklanabilirliği
        val colorQuizGame: ImageView = findViewById(R.id.colorQuizGame)
        colorQuizGame.setOnClickListener {
            val intent = Intent(this, ColorQuizActivity::class.java)
            startActivity(intent)
        }

        // Match Pairs oyunu tıklanabilirliği
        val matchPairsGame: ImageView = findViewById(R.id.matchPairsGame)
        matchPairsGame.setOnClickListener {
            val intent = Intent(this, MatchPairsActivity::class.java)
            startActivity(intent)
        }

        // Hikaye Tamamlama oyunu tıklanabilirliği
        val storyFillGame: ImageView = findViewById(R.id.storyFillGame)
        storyFillGame.setOnClickListener { // Buradaki sorun düzeltiliyor
            val intent = Intent(this, StoryFillActivity::class.java)
            startActivity(intent)
        }

    }
}
