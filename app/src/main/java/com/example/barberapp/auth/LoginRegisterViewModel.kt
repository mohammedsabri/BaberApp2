package com.example.barberapp.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.barberapp.firebase.FirebaseAuthManager
import com.google.firebase.auth.FirebaseUser






class LoginRegisterViewModel (app: Application) : AndroidViewModel(app) {

    /**
     * It creates a new instance of the FirebaseAuthManager class and assigns it to the
     * firebaseAuthManager variable.
     *
     * @param email The email of the user
     * @param password The password for the account.
     */
    var firebaseAuthManager : FirebaseAuthManager = FirebaseAuthManager(app)
    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser

    fun login(email: String?, password: String?) {
        firebaseAuthManager.login(email, password)
    }

    fun register(email: String?, password: String?) {
        firebaseAuthManager.register(email, password)
    }
}

