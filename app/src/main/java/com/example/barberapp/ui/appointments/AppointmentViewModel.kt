package com.example.barberapp.ui.appointments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.barberapp.firebase.FirebaseDBManager
import com.example.barberapp.models.BookManager
import com.example.barberapp.models.BookModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber
import java.lang.Exception

class AppointmentViewModel : ViewModel() {

    private val booksList = MutableLiveData<List<BookModel>>()

    val user = FirebaseAuth.getInstance().currentUser


    val observableBooksList: LiveData<List<BookModel>>
        get() = booksList


    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    var readOnly = MutableLiveData(false)

    var searchResults = ArrayList<BookModel>()

    init {
        load()
    }

    fun load() {
        try {
            readOnly.value = false
            FirebaseDBManager.findAll(user?.uid!!,
                booksList)
            Timber.i("Appointment Load Success : ${booksList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Appointment Load Error : $e.message")
        }

    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(booksList)
            Timber.i("Appointment LoadAll Success : ${booksList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Appointment LoadAll Error : $e.message")
        }
    }
    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid,id)
            Timber.i("Appointment Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Appointment Delete Error : $e.message")
        }
    }

}