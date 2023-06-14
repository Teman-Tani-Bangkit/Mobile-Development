package com.dicoding.temantani.ui.market

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.temantani.adapter.Produk
import com.dicoding.temantani.adapter.ProdukAdapter
import com.dicoding.temantani.api_settings.response.DataItem
import com.dicoding.temantani.databinding.ActivityMarketBinding
import com.dicoding.temantani.helper.ViewModelFactory
import com.dicoding.temantani.models.ProdukViewModel

class MarketActivity : AppCompatActivity() {

    private lateinit var produkViewModel: ProdukViewModel

    private var _activityMarketBinding : ActivityMarketBinding ?= null

    private val binding get() = _activityMarketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMarketBinding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Pembuatan RecylerView
        val layoutManager = GridLayoutManager(this, 2)
        binding?.rvProduk?.layoutManager = layoutManager

        // Pembuatan Stories View Model
        produkViewModel = obtainViewModel(this@MarketActivity)

        produkViewModel.produkResponse.observe(this){response ->
            setCardProdukData(response.data)
        }

        produkViewModel.isLoading.observe(this){
            showLoading(it)
        }

        setupView()
        binding?.apply {
            kategoriTanaman.setOnClickListener { produkViewModel.fetchProdukByKategori("1") }
            kategoriAlat.setOnClickListener { produkViewModel.fetchProdukByKategori("2") }
        }
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
}