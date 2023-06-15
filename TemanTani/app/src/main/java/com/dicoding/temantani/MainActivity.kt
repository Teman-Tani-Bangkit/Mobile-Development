package com.dicoding.temantani

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.temantani.adapter.Produk
import com.dicoding.temantani.adapter.ProdukAdapter
import com.dicoding.temantani.api_settings.response.DataItem
import com.dicoding.temantani.databinding.ActivityMainBinding
import com.dicoding.temantani.helper.ViewModelFactory
import com.dicoding.temantani.models.ProdukViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var produkViewModel: ProdukViewModel

    private var _activityMainBinding : ActivityMainBinding ?= null

    private val binding get() = _activityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Pembuatan RecylerView
        val layoutManagerTanaman = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val layoutManagerAlatTani = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        binding?.rvHomeTanaman?.layoutManager = layoutManagerTanaman
        binding?.rvHomeAlat?.layoutManager = layoutManagerAlatTani

        // Pembuatan Stories View Model
        produkViewModel = obtainViewModel(this@MainActivity)

        produkViewModel.produkTanamanResponse.observe(this){response ->
            setCardTanamanData(response.data.take(6))
        }

        produkViewModel.produkAlatTaniResponse.observe(this){response ->
            setCardAlatTaniData(response.data.take(6 ))
        }
    }

    private fun setCardAlatTaniData(produkResponse: List<DataItem>){
        val listCard = generateCardProduk(produkResponse)
        val adapter = ProdukAdapter(listCard)

        binding?.rvHomeAlat?.adapter = adapter
    }

    private fun setCardTanamanData(produkResponse: List<DataItem>){
        val listCard = generateCardProduk(produkResponse)
        val adapter = ProdukAdapter(listCard)

        binding?.rvHomeTanaman?.adapter = adapter
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
}