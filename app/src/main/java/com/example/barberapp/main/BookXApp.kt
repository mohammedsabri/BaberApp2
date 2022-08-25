package com.example.barberapp.main

import android.app.Application
import android.util.Log
import com.example.barberapp.models.BookMemStore
import com.example.barberapp.models.BookStore


class BookXApp : Application() {

    lateinit var booksStore: BookStore

    override fun onCreate() {
        super.onCreate()
        booksStore = BookMemStore()
        Log.println(Log.INFO,"","BookX Application Started")
    }
}