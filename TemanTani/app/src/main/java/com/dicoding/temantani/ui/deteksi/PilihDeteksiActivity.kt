package com.dicoding.temantani.ui.deteksi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.temantani.R
import com.dicoding.temantani.databinding.ActivityMarketBinding
import com.dicoding.temantani.databinding.ActivityPilihDeteksiBinding
import com.dicoding.temantani.ui.detail.DetailActivity

class PilihDeteksiActivity : AppCompatActivity() {
    private var _activityPilihDeteksiBinding: ActivityPilihDeteksiBinding?= null

    private val binding get() = _activityPilihDeteksiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityPilihDeteksiBinding = ActivityPilihDeteksiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.apply {
            padi.setOnClickListener { selectPadi() }
            singkong.setOnClickListener { selectSingkong() }
            kentang.setOnClickListener { selectKentang() }
            tomat.setOnClickListener { selectTomat() }
            jagung.setOnClickListener { selectJagung() }
            cabai.setOnClickListener { selectCabai() }
            iconBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
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
}