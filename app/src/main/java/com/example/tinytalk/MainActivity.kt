package com.example.tinytalk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavigation()
        setupCardListeners()
    }

    private fun setupBottomNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_dictionary -> {
                    startActivity(Intent(this, DictionaryActivity::class.java))
                    true
                }
                R.id.navigation_chatbot -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                    true
                }
                R.id.navigation_game ->{
                    startActivity(Intent(this, GameMenuActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupCardListeners() {
        val level1Card = findViewById<CardView>(R.id.level1Card)
        level1Card.setOnClickListener {
            val intent = Intent(this, AnimalsActivity::class.java)
            startActivity(intent)
        }

        val level2Card = findViewById<CardView>(R.id.level2Card)
        level2Card.setOnClickListener {
            val intent = Intent(this, FruitsActivity::class.java)
            startActivity(intent)
        }
        val level3Card = findViewById<CardView>(R.id.level3Card)
        level3Card.setOnClickListener {
            val intent = Intent(this, ColorsActivity::class.java)
            startActivity(intent)
        }


    }
}
