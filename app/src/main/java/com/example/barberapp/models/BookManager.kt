package com.example.barberapp.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber


object BookManager : BookStore {

    var lastId = 0L

    internal fun getId(): Long {
        return lastId++
    }
    private val books = ArrayList<BookModel>()

   /* override fun findAll(): List<BookModel> {
        return books
    }

    override fun findById(id:Long) : BookModel? {
        val foundBook: BookModel? = books.find { it.id == id }
        return foundBook
    }

    override fun create(book: BookModel) {
        book.id = getId()
        books.add(book)
        logAll()
    }
*/
    fun logAll() {
        Timber.v("** Appointments List **")
        books.forEach { Timber.v("Appointment ${it}") }
    }

    override fun findAll(booksList: MutableLiveData<List<BookModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, booksList: MutableLiveData<List<BookModel>>) {
        TODO("Not yet implemented")
    }

    override fun findById(userid: String, bookid: String, book: MutableLiveData<BookModel>) {
        TODO("Not yet implemented")
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, book: BookModel) {
        TODO("Not yet implemented")
    }

    override fun delete(userid: String, bookid: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, bookid: String, book: BookModel) {
        TODO("Not yet implemented")
    }
}