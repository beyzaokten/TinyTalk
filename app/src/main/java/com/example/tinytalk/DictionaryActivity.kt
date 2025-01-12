package com.example.tinytalk

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class DictionaryActivity : AppCompatActivity() {

    private lateinit var wordRecyclerView: RecyclerView
    private lateinit var wordAdapter: WordAdapter
    private lateinit var dataSource: DictionaryDataSource
    private var wordList: List<WordEntry> = emptyList()

    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)

        wordRecyclerView = findViewById(R.id.wordRecyclerView)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val searchIcon = findViewById<ImageView>(R.id.searchIcon)

        dataSource = DictionaryDataSource(this)
        wordList = dataSource.loadDictionary()

        wordRecyclerView.layoutManager = LinearLayoutManager(this)
        wordAdapter = WordAdapter(wordList)
        wordRecyclerView.adapter = wordAdapter

        // Arama kutusu dinleme
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchRunnable?.let { searchHandler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    val searchQuery = s.toString()
                    filterWords(searchQuery)
                }
                searchHandler.postDelayed(searchRunnable!!, 300) // 300ms gecikme
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        //Arama tuşu
        searchIcon.setOnClickListener {
            val searchQuery = searchEditText.text.toString()
            filterWords(searchQuery)
        }
        setupBottomNavigation()
    }

    private fun filterWords(query: String) {
        val filteredList = wordList.filter { word ->
            word.turkishWord.contains(query, ignoreCase = true) || word.englishMeaning.contains(query, ignoreCase = true)
        }
        wordAdapter.updateData(filteredList)
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
}
