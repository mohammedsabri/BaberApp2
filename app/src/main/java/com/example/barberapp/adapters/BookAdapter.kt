package com.example.barberapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.barberapp.R
import com.example.barberapp.databinding.CardBookBinding
import com.example.barberapp.models.BookModel

interface BookClickListener {
    fun onBookClick(book: BookModel)
}

class BookAdapter constructor(private var books: ArrayList<BookModel>)
    : RecyclerView.Adapter<BookAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardBookBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val book = books[holder.adapterPosition]
        holder.bind(book)
    }


    fun removeAt(position: Int) {
        books.removeAt(position)
        notifyItemRemoved(position)
    }




    override fun getItemCount(): Int = books.size

    inner class MainHolder(val binding : CardBookBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(book: BookModel) {
            binding.root.tag = book
            binding.book = book
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
          //  binding.root.setOnClickListener { listener.onBookClick(book) }
              binding.executePendingBindings()
        }
    }
}