package com.example.barberapp.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface BookStore {

    fun findAll(booksList:
                MutableLiveData<List<BookModel>>
    )
    fun findAll(userid:String,
                booksList:
                MutableLiveData<List<BookModel>>
    )
    fun findById(userid:String, bookid: String,
                 book: MutableLiveData<BookModel>
    )
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, book: BookModel)
    fun delete(userid:String, bookid: String)
    fun update(userid:String, bookid: String, book: BookModel)
}