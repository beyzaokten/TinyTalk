package com.example.tinytalk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tinytalk.databinding.WordItemBinding

class WordAdapter(private var words: List<WordEntry>) :
    RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    class WordViewHolder(private val binding: WordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(word: WordEntry) {
            binding.turkishTextView.text = word.turkishWord
            binding.englishTextView.text = word.englishMeaning
            binding.categoryTextView.text = word.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = WordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(words[position])
    }

    override fun getItemCount(): Int = words.size

    fun updateData(newWords: List<WordEntry>) {
        words = newWords
        notifyDataSetChanged()
    }
}
