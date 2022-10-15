package com.siddydevelops.instastoryassignment.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var data: ArrayList<UserData>
) : Parcelable

@Parcelize
data class UserData(
    val image: String,
    val type: String
) : Parcelable
