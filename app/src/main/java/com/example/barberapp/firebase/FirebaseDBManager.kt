package com.example.barberapp.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.barberapp.models.BookModel
import com.example.barberapp.models.BookStore
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import timber.log.Timber

object FirebaseDBManager : BookStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance("https://barber-app-bb86c-default-rtdb.europe-west1.firebasedatabase.app/").reference

    override fun findAll(booksList: MutableLiveData<List<BookModel>>) {
        database.child("books")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Book error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<BookModel>()
                    val children = snapshot.children
                    children.forEach {
                        val Book = it.getValue(BookModel::class.java)
                        localList.add(Book!!)
                    }
                    database.child("books")
                        .removeEventListener(this)

                    booksList.value = localList
                }
            })
    }

    override fun findAll(userid: String, booksList: MutableLiveData<List<BookModel>>) {

        database.child("user-books").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Book error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<BookModel>()
                    val children = snapshot.children
                    children.forEach {
                        val book = it.getValue(BookModel::class.java)
                        localList.add(book!!)
                    }
                    database.child("books").child(userid)
                        .removeEventListener(this)

                    booksList.value = localList
                }
            })
    }

    override fun findById(userid: String, bookid: String, book: MutableLiveData<BookModel>) {

        database.child("user-books").child(userid)
            .child(bookid).get().addOnSuccessListener {
                book.value = it.getValue(BookModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, book: BookModel) {
        Timber.i("Firebase DB Reference : $database")
        Log.println(Log.INFO, "INFO","In creaate - INFO")
        Log.println(Log.INFO, "INFO", firebaseUser.value?.uid.toString())
        Log.println(Log.INFO, "INFO","Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("books").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        book.uid = key
        val bookValues = book.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/books/$key"] = bookValues
        childAdd["/user-books/$uid/$key"] = bookValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, bookid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/books/$bookid"] = null
        childDelete["/user-books/$userid/$bookid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, bookid: String, book: BookModel) {

        val bookValues = book.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["books/$bookid"] = bookValues
        childUpdate["user-books/$userid/$bookid"] = bookValues

        database.updateChildren(childUpdate)
    }

    fun updateImageRef(userid: String,imageUri: String) {

        val userBooks = database.child("user-books").child(userid)
        val allBooks = database.child("books")

        userBooks.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {}
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            //Update Users imageUri
                            it.ref.child("profilepic").setValue(imageUri)
                            //Update all books that match 'it'
                            val book = it.getValue(BookModel::class.java)
                            allBooks.child(book!!.uid!!)
                                         .child("profilepic").setValue(imageUri)
                        }
                    }
                })
    }


}