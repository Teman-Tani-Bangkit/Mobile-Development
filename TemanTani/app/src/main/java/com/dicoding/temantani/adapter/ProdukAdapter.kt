package com.dicoding.temantani.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.temantani.databinding.CardProductBinding

class ProdukAdapter(private val allProduk : List<Produk>) : RecyclerView.Adapter<ProdukAdapter.ViewHolder>()  {

    inner class ViewHolder(binding : CardProductBinding) : RecyclerView.ViewHolder(binding.root) {
        val produkImage : ImageView = binding.imageItem
        val produkName : TextView = binding.namaItem
        val produkPrice : TextView = binding.textPrice
//        val storyImage : ImageView = binding.storyImage
//        val userName : TextView = binding.userStoryName
//        val createdDate : TextView = binding.userStoryCreated
//        val readMore : ImageView = binding.btnReadMore
//        val cardView : CardView = binding.cardView
    }

    override fun getItemCount(): Int = allProduk.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardProductBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val(img, nama, harga) = allProduk[position]

        Glide.with(holder.itemView)
            .load("https://storage.googleapis.com/ml-temantani/potato-early-blight-lesions.jpeg")
            .into(holder.produkImage)

        holder.produkName.text = nama
        holder.produkPrice.text = harga
    }

}















