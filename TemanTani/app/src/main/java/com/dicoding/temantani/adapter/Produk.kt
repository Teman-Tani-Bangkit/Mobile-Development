package com.dicoding.temantani.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Produk (
    var image : String,
    var namaProduk : String,
    var hargaProduk : String,
    var idProduk : String,
    var idUser : String,
    var deskripsiProduk : String
) : Parcelable