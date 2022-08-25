package com.example.barberapp.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize
import java.time.LocalDate.now
import java.time.LocalDateTime
import java.util.*


@Parcelize
data class BookModel(
    var id: Long = 0,
    var profilepic: String = "",
    var uid: String = "",
    val service: String = "haircut",
    val appDate: String = "n/A",
    val time: String = "N/z",
    val email: String = "moe@sabri.com",
    val barbername: String ="moe",
    val cost: Int = 15) : Parcelable


{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "service" to service  ,
            "appDate" to appDate,
            "time" to time,
            "barbername" to barbername,
            "cost" to cost,
            "profilepic" to profilepic,
            "email" to email,
//            "latitude" to latitude,
//            "longitude" to longitude
        )
    }
}