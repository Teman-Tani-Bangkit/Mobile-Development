package com.dicoding.temantani.db

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAuth(
    var name : String ?= null,
    var id : String ?= null,
    var token : String ?= null

) : Parcelable
