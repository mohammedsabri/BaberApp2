package com.example.barberapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.barberapp.firebase.FirebaseDBManager
import com.example.barberapp.firebase.FirebaseImageManager
import com.example.barberapp.models.BookManager
import com.example.barberapp.models.BookModel
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber
import java.lang.Exception

class BookViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addBook(firebaseUser: MutableLiveData<FirebaseUser>,
                book: BookModel) {
        status.value = try {

            //book.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.create(firebaseUser,book)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
    fun updateBook(userid:String, id: String,book: BookModel) {
        try {
            //BookManager.update(email, id, book)
            FirebaseDBManager.update(userid, id, book)
            Timber.i("Detail update() Success : $book")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }

}