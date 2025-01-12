package com.example.tinytalk

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tinytalk.model.Story
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class StoryFillActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var storyImage: ImageView
    private lateinit var storyText: TextView
    private lateinit var missingWordInput: EditText
    private lateinit var submitAnswerButton: Button
    private lateinit var backButton: ImageView
    private lateinit var tts: TextToSpeech

    private val stories = mutableListOf<Story>()
    private var currentStoryIndex = 0
    private var isTtsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_fill)

        storyImage = findViewById(R.id.storyImage)
        storyText = findViewById(R.id.storyText)
        missingWordInput = findViewById(R.id.missingWordInput)
        submitAnswerButton = findViewById(R.id.submitAnswerButton)
        backButton = findViewById(R.id.backButton)

        tts = TextToSpeech(this, this)

        loadStoriesFromJson()
        displayCurrentStory()

        submitAnswerButton.setOnClickListener {
            checkAnswer()
        }

        backButton.setOnClickListener {
            val intent = Intent(this, GameMenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadStoriesFromJson() {
        val inputStream = assets.open("story.json")
        val reader = inputStream.bufferedReader()
        val type = object : TypeToken<List<Story>>() {}.type
        stories.addAll(Gson().fromJson(reader, type))
    }

    private fun displayCurrentStory() {
        val currentStory = stories[currentStoryIndex]
        storyText.text = currentStory.story
        storyImage.setImageResource(resources.getIdentifier(currentStory.image, "drawable", packageName))
        if (isTtsReady) {
            speakStory(currentStory.story)
        }
    }

    private fun checkAnswer() {
        val userAnswer = missingWordInput.text.toString().trim()
        val currentStory = stories[currentStoryIndex]

        if (userAnswer.equals(currentStory.missingWords[0], ignoreCase = true)) {
            Toast.makeText(this, "Doğru!", Toast.LENGTH_SHORT).show()
            if (currentStoryIndex < stories.size - 1) {
                currentStoryIndex++
                missingWordInput.text.clear()
                displayCurrentStory()
            } else {
                showCompletionMessage()
            }
        } else {
            Toast.makeText(this, "Tekrar dene!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCompletionMessage() {
        storyText.text = "Tebrikler! Hikayeyi bitirdin!"
        missingWordInput.visibility = View.GONE
        submitAnswerButton.visibility = View.GONE
        speakStory(storyText.text.toString())
    }

    private fun speakStory(story: String) {
        val words = story.split("__")
        words.forEachIndexed { index, part ->
            tts.speak(part, TextToSpeech.QUEUE_ADD, null, null)
            if (index < words.size - 1) {
                tts.playSilentUtterance(1000, TextToSpeech.QUEUE_ADD, null)
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
            isTtsReady = true
            displayCurrentStory()
        }
    }

    override fun onDestroy() {
        if (tts.isSpeaking) {
            tts.stop()
        }
        tts.shutdown()
        super.onDestroy()
    }
}
