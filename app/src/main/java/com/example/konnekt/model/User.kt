package com.example.konnekt.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val firstname: String, val lastname: String, val username: String, val profileimageurl: String):Parcelable{
    constructor():this("","","","","")
}