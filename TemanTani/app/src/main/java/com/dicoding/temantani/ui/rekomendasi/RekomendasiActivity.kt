package com.dicoding.temantani.ui.rekomendasi

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.temantani.R
import com.dicoding.temantani.databinding.ActivityRekomendasiBinding
import com.dicoding.temantani.db.UserAuth
import com.dicoding.temantani.db.UserPreference
import com.dicoding.temantani.helper.ViewModelFactory
import com.dicoding.temantani.models.RekomendasiViewModel
import com.dicoding.temantani.models.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class RekomendasiActivity : AppCompatActivity() {
    private lateinit var userAuth : UserAuth

    private lateinit var rekomendasiViewModel: RekomendasiViewModel
    private lateinit var weatherViewModel: WeatherViewModel

    private var _activityRekomendasiBinding : ActivityRekomendasiBinding?= null
    private val binding get() = _activityRekomendasiBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    private var temperature : String ?= null
    private var humidity : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityRekomendasiBinding = ActivityRekomendasiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        userAuth()
        TOKEN = userAuth.token.toString()

        // Inisiasi location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Pembuatan View Model
        rekomendasiViewModel = obtainViewModel(this@RekomendasiActivity)
        rekomendasiViewModel.isLoading.observe(this@RekomendasiActivity){
            showLoading(it)
        }
        rekomendasiViewModel.responseMessage.observe(this){
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        rekomendasiViewModel.uploadRekomendasiResponse.observe(this){response ->
            val dialogBinding = layoutInflater.inflate(R.layout.pop_up_rekomendasi, null)
            val popUpRekomendasi = Dialog(this)
            popUpRekomendasi.setContentView(dialogBinding)

            val cropsKiri = dialogBinding.findViewById<TextView>(R.id.text_crops_kiri)
            val cropsTengah = dialogBinding.findViewById<TextView>(R.id.text_crops_tengah)
            val cropsKanan = dialogBinding.findViewById<TextView>(R.id.text_crops_kanan)

            cropsKiri.text = response.crops1
            cropsTengah.text = response.crops2
            cropsKanan.text = response.crops3

            popUpRekomendasi.setCancelable(true)
            popUpRekomendasi.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popUpRekomendasi.show()
        }

        weatherViewModel = obtainWeatherViewModel(this@RekomendasiActivity)
        weatherViewModel.isLoading.observe(this){
            showLoading(it)
        }
        weatherViewModel.responseMessage.observe(this){
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        weatherViewModel.weatherResponse.observe(this){response ->
            temperature = response.main?.temp.toString()
            humidity = response.main?.humidity.toString()
        }

        binding?.apply {
            tvLokasi.setOnClickListener { getMyLastLocation() }
            btnRekomendasi.setOnClickListener { getRekomendasi() }
        }
    }

    private fun getRekomendasi(){
        val edNitrogen = binding?.edNitrogen?.text.toString()
        val edFosfor = binding?.edFosfor?.text.toString()
        val edKalium = binding?.edKalium?.text.toString()
        val edPh = binding?.edPh?.text.toString()
        val edRainfall = binding?.edRainfall?.text.toString()
        val userAuthToken = TOKEN.toString()

        if(edNitrogen.isEmpty() || edFosfor.isEmpty() || edKalium.isEmpty() || edPh.isEmpty() || edRainfall.isEmpty()){
            warnEmptyEd(edNitrogen, edFosfor, edKalium ,edPh, edRainfall)

        } else if(edNitrogen.isNotEmpty() && edFosfor.isNotEmpty() && edKalium.isNotEmpty() && edPh.isNotEmpty() && edRainfall.isNotEmpty()){

            if(temperature != null && humidity != null){
                rekomendasiViewModel.plantReccomendation(
                    userAuthToken,
                    edNitrogen,
                    edFosfor,
                    edKalium,
                    temperature!!,
                    humidity!!,
                    edPh,
                    edRainfall
                )

            } else {
                Toast.makeText(this, "Mohon tentukan daerah anda", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun locationToString(location: Location): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        return addressList?.get(0)?.getAddressLine(0) ?: "Not Found"
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val userLocation = locationToString(location)
                    currentLocation = location
                    weatherViewModel.getWeatherData(location.latitude.toString(), location.longitude.toString())
                    binding?.tvLokasi?.text = userLocation
                } else {
                    Toast.makeText(
                        this@RekomendasiActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun warnEmptyEd(n : String, p : String, k : String, ph : String, rainfall : String){
        if(n.isEmpty()){
            binding?.edNitrogen?.error = "Mohon masukkan nilai Natrium terlebih dahulu"
        }

        if(p.isEmpty()){
            binding?.edFosfor?.error = "Mohon masukkan nilai Fosfor terlebih dahulu"
        }

        if(k.isEmpty()){
            binding?.edKalium?.error = "Mohon masukkan nilai Kalium terlebih dahulu"
        }

        if(ph.isEmpty()){
            binding?.edPh?.error = "Mohon masukkan nilai ph terlebih dahulu"
        }

        if(rainfall.isEmpty()){
            binding?.edRainfall?.error = "Mohon masukkan curah hujan terlebih dahulu"
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity) : RekomendasiViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(RekomendasiViewModel::class.java)
    }

    private fun obtainWeatherViewModel(activity: AppCompatActivity) : WeatherViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(WeatherViewModel::class.java)
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun userAuth(){
        val userPref = UserPreference(this)
        userAuth = userPref.getUser()
    }

    private fun showLoading(state: Boolean) { binding?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE }

    companion object {
        var TOKEN: String? = null
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}