package com.example.tinytalk

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class DictionaryDataSource(private val context: Context) {
    fun loadDictionary(): List<WordEntry> {
        val wordList = mutableListOf<WordEntry>()
        val inputStream = context.assets.open("dictionary.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.forEachLine { line ->
            val parts = line.split(",")
            if (parts.size >= 3) {
                val entry = WordEntry(
                    turkishWord = parts[0].trim(),
                    englishMeaning = parts[1].trim(),
                    category = parts[2].trim()
                )
                wordList.add(entry)
            }
        }
        reader.close()
        return wordList
    }
}
