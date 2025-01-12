package com.example.tinytalk

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tinytalk.model.MatchPair
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MatchPairsActivity : AppCompatActivity() {
    private lateinit var gridLayout: GridLayout
    private val cardList = mutableListOf<MatchPair>()
    private val openedCards = mutableListOf<Pair<RelativeLayout, MatchPair>>()
    private val matchedCards = mutableListOf<Int>()
    private lateinit var wrongSound: MediaPlayer
    private lateinit var winSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_pairs)

        gridLayout = findViewById(R.id.gridLayout)
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener { finish() }

        // Ses dosyalarını yükleme
        wrongSound = MediaPlayer.create(this, R.raw.wrong)
        winSound = MediaPlayer.create(this, R.raw.correct)

        loadCardsFromJson()
        setupGame()
    }

    private fun loadCardsFromJson() {
        val inputStream = assets.open("match_pairs.json")
        val reader = inputStream.bufferedReader()
        val type = object : TypeToken<List<MatchPair>>() {}.type
        val pairs = Gson().fromJson<List<MatchPair>>(reader, type)
        cardList.addAll(pairs)
        cardList.shuffle()
    }

    private fun setupGame() {
        gridLayout.removeAllViews()
        gridLayout.rowCount = 4
        gridLayout.columnCount = 3
        gridLayout.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        }

        cardList.forEach { pair ->
            val card = createCard(pair)
            gridLayout.addView(card)
        }
    }

    private fun createCard(pair: MatchPair): RelativeLayout {
        val cardLayout = RelativeLayout(this).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 200
                height = 200
                marginStart = 8
                marginEnd = 8
                bottomMargin = 8
            }
        }

        val cardBack = ImageView(this).apply {
            setImageResource(R.drawable.card_back)
            scaleType = ImageView.ScaleType.CENTER_CROP
            id = View.generateViewId()
        }

        val cardText = TextView(this).apply {
            text = "" // İlk başta metin yok
            textSize = 18f
            setTextColor(resources.getColor(android.R.color.black, theme))
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            gravity = android.view.Gravity.CENTER
            visibility = View.INVISIBLE
            background = resources.getDrawable(R.drawable.white, theme)
            id = View.generateViewId()


            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        }

        cardLayout.addView(cardBack)
        cardLayout.addView(cardText)

        cardLayout.setOnClickListener {
            onCardClicked(cardLayout, pair)
        }

        return cardLayout
    }

    private fun onCardClicked(cardLayout: RelativeLayout, pair: MatchPair) {
        if (matchedCards.contains(pair.id) || openedCards.any { it.first == cardLayout }) return

        showCardContent(cardLayout, pair)
        openedCards.add(cardLayout to pair)

        if (openedCards.size == 2) {
            val (firstCardLayout, firstPair) = openedCards[0]
            val (secondCardLayout, secondPair) = openedCards[1]

            Handler().postDelayed({
                if (firstPair.id == secondPair.id) {
                    matchedCards.add(firstPair.id)
                    firstCardLayout.visibility = View.INVISIBLE
                    secondCardLayout.visibility = View.INVISIBLE
                } else {
                    wrongSound.start() // Yanlış ses çal
                    resetCard(firstCardLayout)
                    resetCard(secondCardLayout)
                }
                openedCards.clear()
                if (matchedCards.size == cardList.size / 2) {
                    winSound.start() // Kazanma sesi çal
                    showGameOverMessage()
                }
            }, 1000)
        }
    }

    private fun showCardContent(cardLayout: RelativeLayout, pair: MatchPair) {
        val cardBack = cardLayout.getChildAt(0) as ImageView
        val cardText = cardLayout.getChildAt(1) as TextView

        if (pair.type == "image") {
            cardBack.setImageResource(resources.getIdentifier(pair.image, "drawable", packageName))
        } else if (pair.type == "text") {
            cardBack.visibility = View.INVISIBLE
            cardText.text = pair.name
            cardText.visibility = View.VISIBLE
        }
    }

    private fun resetCard(cardLayout: RelativeLayout) {
        val cardBack = cardLayout.getChildAt(0) as ImageView
        val cardText = cardLayout.getChildAt(1) as TextView

        cardBack.visibility = View.VISIBLE
        cardBack.setImageResource(R.drawable.card_back)
        cardText.visibility = View.INVISIBLE
    }

    @SuppressLint("NewApi")
    private fun showGameOverMessage() {
        val message = TextView(this).apply {
            text = "Tebrikler! Tüm kartları eşleştirdin!"
            textSize = 24f
            setTextColor(resources.getColor(android.R.color.white, theme))
            typeface = resources.getFont(R.font.modak)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        gridLayout.removeAllViews()
        gridLayout.addView(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (wrongSound.isPlaying) wrongSound.stop()
        if (winSound.isPlaying) winSound.stop()
        wrongSound.release()
        winSound.release()
    }
}
