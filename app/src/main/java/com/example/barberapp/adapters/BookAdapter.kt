package com.example.barberapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.barberapp.R
import com.example.barberapp.databinding.CardBookBinding
import com.example.barberapp.models.BookModel
import com.example.barberapp.utils.customTransformation
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

interface BookClickListener {
    fun onBookClick(book: BookModel)
}

class BookAdapter constructor(private var books: ArrayList<BookModel>,
                              private val listener: BookClickListener,
                              private val readOnly: Boolean)

    : RecyclerView.Adapter<BookAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardBookBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding,readOnly)
    }


    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val book = books[holder.adapterPosition]
        holder.bind(book,listener)
    }


    fun removeAt(position: Int) {
        books.removeAt(position)
        notifyItemRemoved(position)
    }




    override fun getItemCount(): Int = books.size

    /* The MainHolder class is a RecyclerView.ViewHolder that holds a CardBookBinding object */
    inner class MainHolder(val binding : CardBookBinding, private val readOnly: Boolean) :
        RecyclerView.ViewHolder(binding.root) {
        val readOnlyRow = readOnly

        fun bind(book: BookModel, listener: BookClickListener) {
            binding.root.tag = book
            binding.book = book

            Picasso.get().load(book.profilepic.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(binding.imageIcon)


            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_moe_round)


            binding.root.setOnClickListener { listener.onBookClick(book) }
              binding.executePendingBindings()
        }
    }
}