package com.dicoding.temantani

import android.content.Intent
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
import com.dicoding.temantani.db.UserAuth
import com.dicoding.temantani.db.UserPreference
import com.dicoding.temantani.helper.ViewModelFactory
import com.dicoding.temantani.models.ProdukViewModel
import com.dicoding.temantani.ui.deteksi.DeteksiTanamanActivity
import com.dicoding.temantani.ui.deteksi.PilihDeteksiActivity
import com.dicoding.temantani.ui.market.MarketActivity
import com.dicoding.temantani.ui.profile.ProfileActivity
import com.dicoding.temantani.ui.rekomendasi.RekomendasiActivity
import com.dicoding.temantani.ui.upload.UploadProductActivity

class MainActivity : AppCompatActivity() {
    private lateinit var userAuth : UserAuth

    private lateinit var produkViewModel: ProdukViewModel

    private var _activityMainBinding : ActivityMainBinding ?= null

    private val binding get() = _activityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        userAuth()
        TOKEN = userAuth.token.toString()

        // Pembuatan RecylerView
        val layoutManagerTanaman = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val layoutManagerAlatTani = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        binding?.rvHomeTanaman?.layoutManager = layoutManagerTanaman
        binding?.rvHomeAlat?.layoutManager = layoutManagerAlatTani

        // Pembuatan Stories View Model
        produkViewModel = obtainViewModel(this@MainActivity)

        produkViewModel.produkTanamanResponse.observe(this){response ->
            setCardTanamanData(response.data.reversed().take(6))
        }

        produkViewModel.produkAlatTaniResponse.observe(this){response ->
            setCardAlatTaniData(response.data.reversed().take(6))
        }

        binding?.apply {
            tvTextSeeMoreAlat.setOnClickListener { moveToMarket() }
            tvTextSeeMoreTanaman.setOnClickListener { moveToMarket() }
            imgProfile.setOnClickListener { moveToProfile() }
            imgToDeteksi.setOnClickListener { moveToPilihDeteksi() }
            imgToRekomendasi.setOnClickListener { moveToRekomendasi() }
            imgUpload.setOnClickListener { moveToUpload() }

            padi.setOnClickListener { selectPadi() }
            singkong.setOnClickListener { selectSingkong() }
            kentang.setOnClickListener { selectKentang() }
            tomat.setOnClickListener { selectTomat() }
            jagung.setOnClickListener { selectJagung() }
            cabai.setOnClickListener { selectCabai() }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun moveToUpload(){
        val intentToUpload= Intent(this@MainActivity, UploadProductActivity::class.java)
        startActivity(intentToUpload)
    }

    private fun moveToPilihDeteksi(){
        val intentToPilihDeteksi = Intent(this@MainActivity, PilihDeteksiActivity::class.java)
        startActivity(intentToPilihDeteksi)
    }

    private fun moveToProfile(){
        val intentToProfile = Intent(this@MainActivity, ProfileActivity::class.java)
        startActivity(intentToProfile)
    }

    private fun moveToMarket(){
        val intentToMarket = Intent(this@MainActivity, MarketActivity::class.java)
        startActivity(intentToMarket)
    }

    private fun moveToRekomendasi(){
        val intentToRekomendasi = Intent(this@MainActivity, RekomendasiActivity::class.java)
        startActivity(intentToRekomendasi)
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

    private fun selectPadi(){
        val intentWithPadi = Intent(this, DeteksiTanamanActivity::class.java)
        intentWithPadi.putExtra(DeteksiTanamanActivity.CATEGORY_DETECT, "Rice")
        startActivity(intentWithPadi)
    }

    private fun selectSingkong(){
        val intentWithSingkong = Intent(this, DeteksiTanamanActivity::class.java)
        intentWithSingkong.putExtra(DeteksiTanamanActivity.CATEGORY_DETECT, "Cassava")
        startActivity(intentWithSingkong)
    }

    private fun selectKentang(){
        val intentWithKentang = Intent(this, DeteksiTanamanActivity::class.java)
        intentWithKentang.putExtra(DeteksiTanamanActivity.CATEGORY_DETECT, "Potato")
        startActivity(intentWithKentang)
    }

    private fun selectTomat(){
        val intentWithTomat = Intent(this, DeteksiTanamanActivity::class.java)
        intentWithTomat. putExtra(DeteksiTanamanActivity.CATEGORY_DETECT, "Tomato")
        startActivity(intentWithTomat)
    }

    private fun selectJagung(){
        val intentWithJagung = Intent(this, DeteksiTanamanActivity::class.java)
        intentWithJagung.putExtra(DeteksiTanamanActivity.CATEGORY_DETECT, "Corn")
        startActivity(intentWithJagung)
    }
    private fun selectCabai(){
        val intentWithCabai = Intent(this, DeteksiTanamanActivity::class.java)
        intentWithCabai.putExtra(DeteksiTanamanActivity.CATEGORY_DETECT, "Chili")
        startActivity(intentWithCabai)
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

    companion object{
        var TOKEN : String ?= null
    }
}