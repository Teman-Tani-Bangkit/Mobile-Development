package com.dicoding.temantani.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.temantani.R
import com.dicoding.temantani.api_settings.response.DetailResponse
import com.dicoding.temantani.databinding.ActivityDetailBinding
import com.dicoding.temantani.db.UserAuth
import com.dicoding.temantani.db.UserPreference
import com.dicoding.temantani.helper.ViewModelFactory
import com.dicoding.temantani.models.DetailViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var userAuth : UserAuth

    private var _activityDetailBinding : ActivityDetailBinding?= null
    private val binding get() = _activityDetailBinding

    private lateinit var detailViewModel: DetailViewModel

    private lateinit var noTelepon : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       _activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        userAuth()
        TOKEN = userAuth.token.toString()
        val idProduk = intent.getStringExtra(ID)
        ID = idProduk

        detailViewModel = obtainViewModel(this@DetailActivity)

        detailViewModel.produkDetailResponse.observe(this){
            setDetailData(it)
        }

        detailViewModel.isLoading.observe(this){
            showLoading(it)
        }

        binding?.apply {
            btnPesan.setOnClickListener { kontakPenjual(noTelepon) }
            iconBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
    }

    private fun kontakPenjual(noTelepon : String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/+62$noTelepon?text=Halo%20saya%20ingin%20memesan%20produk"))
        startActivity(intent)
    }

    private fun setDetailData(detailDataResponse : DetailResponse){

        binding?.imageItem?.let {
            Glide.with(this)
                .load("https://storage.googleapis.com/temantani-bucket/barang/${detailDataResponse.data?.gambarbarang}")
                .into(it)
        }

        binding?.namaItem?.text = detailDataResponse.data?.namabarang
        binding?.userName?.text = detailDataResponse.data?.nama
        binding?.deskripsiItem?.text = detailDataResponse.data?.deskripsi
        binding?.hargaItem?.text = detailDataResponse.data?.harga

        if (detailDataResponse.data?.kategori == 2){
            binding?.textKilogram?.setText(R.string.pcs)
        }

        noTelepon = detailDataResponse.data?.notelepon.toString()
    }

    private fun obtainViewModel(activity: AppCompatActivity) : DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    private fun userAuth(){
        val userPref = UserPreference(this)
        userAuth = userPref.getUser()
    }

    private fun showLoading(state: Boolean) { binding?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE }
    companion object{
        var TOKEN : String ?= null
        var ID : String ?= null
    }
}






























