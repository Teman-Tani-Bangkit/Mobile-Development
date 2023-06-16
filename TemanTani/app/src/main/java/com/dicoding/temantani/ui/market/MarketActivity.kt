package com.dicoding.temantani.ui.market

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.temantani.adapter.Produk
import com.dicoding.temantani.adapter.ProdukAdapter
import com.dicoding.temantani.api_settings.response.DataItem
import com.dicoding.temantani.databinding.ActivityMarketBinding
import com.dicoding.temantani.db.UserAuth
import com.dicoding.temantani.db.UserPreference
import com.dicoding.temantani.helper.ViewModelFactory
import com.dicoding.temantani.models.ProdukViewModel
import com.dicoding.temantani.ui.profile.ProfileActivity
import java.io.File

class MarketActivity : AppCompatActivity() {
    private lateinit var userAuth : UserAuth

    private lateinit var produkViewModel: ProdukViewModel

    private var _activityMarketBinding : ActivityMarketBinding ?= null

    private val binding get() = _activityMarketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMarketBinding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        userAuth()
        TOKEN = userAuth.token.toString()

        // Pembuatan RecylerView
        val layoutManager = GridLayoutManager(this, 2)
        binding?.rvProduk?.layoutManager = layoutManager

        // Pembuatan Stories View Model
        produkViewModel = obtainViewModel(this@MarketActivity)

        produkViewModel.produkResponse.observe(this){response ->
            setCardProdukData(response.data.reversed())
        }

        produkViewModel.isLoading.observe(this){
            showLoading(it)
        }

        setupView()
        binding?.apply {
            kategoriTanaman.setOnClickListener { produkViewModel.fetchProdukByKategori("1", TOKEN!!) }
            kategoriAlat.setOnClickListener { produkViewModel.fetchProdukByKategori("2", TOKEN!!) }
            imgProfile.setOnClickListener { moveToProfile() }

            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                private var isSearching = false

                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query != null) produkViewModel.searchProduk(TOKEN!!, query)
                    isSearching = true
                    binding?.searchBar!!.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty() && isSearching == true) {
                        produkViewModel.fetchProduk(TOKEN!!)
                        isSearching = false
                        binding?.searchBar!!.clearFocus()
                    }
                    return true
                }

            })
        }
    }

    private fun moveToProfile(){
        val intentToProfile = Intent(this@MarketActivity, ProfileActivity::class.java)
        startActivity(intentToProfile)
    }

    private fun setCardProdukData(produkResponse: List<DataItem>){
        val listCard = generateCardProduk(produkResponse)
        val adapter = ProdukAdapter(listCard)
        binding?.rvProduk?.adapter = adapter
    }

    private fun generateCardProduk(produkResponse: List<DataItem>) : ArrayList<Produk>{
        val listCard= ArrayList<Produk>()

        for(produkData in produkResponse){
            val cardProduk = Produk(
                produkData.gambarbarang.toString(),
                produkData.namabarang.toString(),
                produkData.harga.toString(),
                produkData.idbarang.toString(),
                produkData.userid.toString(),
                produkData.deskripsi.toString()
            )

            listCard.add(cardProduk)
        }
        return listCard
    }

    private fun userAuth(){
        val userPref = UserPreference(this)
        userAuth = userPref.getUser()
    }

    private fun obtainViewModel(activity: AppCompatActivity) : ProdukViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(ProdukViewModel::class.java)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showLoading(state: Boolean) { binding?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE }

    companion object{
        var TOKEN : String ?= null
    }
}