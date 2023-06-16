package com.dicoding.temantani.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.temantani.databinding.CardProductBinding
import com.dicoding.temantani.ui.detail.DetailActivity

class ProdukAdapter(private val allProduk : List<Produk>) : RecyclerView.Adapter<ProdukAdapter.ViewHolder>()  {

    inner class ViewHolder(binding : CardProductBinding) : RecyclerView.ViewHolder(binding.root) {
        val produkImage : ImageView = binding.imageItem
        val produkName : TextView = binding.namaItem
        val produkPrice : TextView = binding.textPrice
    }

    override fun getItemCount(): Int = allProduk.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardProductBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val(img, nama, harga) = allProduk[position]

        Glide.with(holder.itemView)
            .load("https://storage.googleapis.com/temantani-bucket/barang/$img")
            .into(holder.produkImage)

        holder.produkName.text = nama
        holder.produkPrice.text = harga

        holder.itemView.setOnClickListener {
            val intentToDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentToDetail.putExtra(DetailActivity.ID, allProduk[holder.adapterPosition].idProduk)
            holder.itemView.context.startActivity(intentToDetail)
        }
    }

}















