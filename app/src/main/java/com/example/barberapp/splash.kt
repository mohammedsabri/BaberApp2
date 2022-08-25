package com.example.barberapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.barberapp.auth.Login


class splash : AppCompatActivity() {
//    var appname: ImageView? = null
//    var splashimg: ImageView? = null
    var lottieAnimationView: LottieAnimationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN

        )

//        appname = findViewById(R.id.app_name)
//        splashimg = findViewById(R.id.img)
        lottieAnimationView = findViewById(R.id.lottie)
//        splashimg.animate().translationY(-2500f).setDuration(1000).startDelay = 5000
//        appname.animate().translationY(2000f).setDuration(1000).startDelay = 5000
        lottieAnimationView!!.animate().translationY(1500f).setDuration(1000).startDelay = 5000
        Handler().postDelayed(
            { startActivity(Intent(this@splash, Login::class.java)) },
            6000
        )
    }
}