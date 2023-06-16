package com.dicoding.temantani.ui.profile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.temantani.adapter.Produk
import com.dicoding.temantani.adapter.ProdukAdapter
import com.dicoding.temantani.api_settings.response.DataItemProfile
import com.dicoding.temantani.api_settings.response.ProfileResponse
import com.dicoding.temantani.databinding.ActivityProfileBinding
import com.dicoding.temantani.db.UserAuth
import com.dicoding.temantani.db.UserPreference
import com.dicoding.temantani.helper.ViewModelFactory
import com.dicoding.temantani.models.ProfileViewModel
import com.dicoding.temantani.ui.detail.DetailActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var userAuth : UserAuth

    private var _activityProfileBinding : ActivityProfileBinding?= null
    private val binding get() = _activityProfileBinding

    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityProfileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        userAuth()
        TOKEN = userAuth.token.toString()
        ID = userAuth.id.toString()

        profileViewModel = obtainViewModel(this@ProfileActivity)

        profileViewModel.profileResponse.observe(this){response ->
            setProfileData(response)
        }

        profileViewModel.isLoading.observe(this){
            showLoading(it)
        }

    }

    private fun setProfileData(profileDataResponse : ProfileResponse){

        binding?.textUsername?.text = profileDataResponse.data?.user?.nama
        binding?.textUseremail?.text = profileDataResponse.data?.user?.email

        profileDataResponse.data?.let { setCardProdukData(it.barang) }
    }

    private fun setCardProdukData(produkResponse: List<DataItemProfile>){
        val listCard = generateCardProduk(produkResponse)
        val adapter = ProdukAdapter(listCard)
        binding?.rvProdukProfile?.adapter = adapter
    }

    private fun generateCardProduk(produkResponse: List<DataItemProfile>) : ArrayList<Produk>{
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

    private fun obtainViewModel(activity: AppCompatActivity) : ProfileViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(ProfileViewModel::class.java)
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