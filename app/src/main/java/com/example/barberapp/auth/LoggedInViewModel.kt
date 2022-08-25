package com.example.barberapp.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.barberapp.firebase.FirebaseAuthManager
import com.google.firebase.auth.FirebaseUser

class LoggedInViewModel(app: Application) : AndroidViewModel(app) {

    /* This is creating a new instance of the FirebaseAuthManager class and assigning it to the
    variable firebaseAuthManager. It is also creating a new instance of the MutableLiveData class
    and assigning it to the variable liveFirebaseUser. */
    var firebaseAuthManager : FirebaseAuthManager = FirebaseAuthManager(app)
    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser
    var loggedOut : MutableLiveData<Boolean> = firebaseAuthManager.loggedOut

    fun logOut() {
        firebaseAuthManager.logOut()
    }
}