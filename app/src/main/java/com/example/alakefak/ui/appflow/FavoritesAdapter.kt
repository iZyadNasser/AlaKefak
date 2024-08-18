package com.example.alakefak.ui.appflow

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.alakefak.R
import com.example.alakefak.data.source.local.model.FavoritesInfo

class FavoritesAdapter(var items: List<FavoritesInfo>) : RecyclerView.Adapter<FavoritesAdapter.MyViewHolder>() {
    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        var recipeName: TextView = row.findViewById(R.id.nameTextView)
        var recipeCategory: TextView = row.findViewById(R.id.categoryTextView)
        var recipeImg: ImageView = row.findViewById(R.id.recipeFavImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.my_fav_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items.getOrNull(position)
        if (item != null) {
            holder.recipeName.text = item.recipeName
            holder.recipeCategory.text = item.recipeCategory
            Glide.with(holder.itemView.context)
                .load(item.recipeImg)
                .transform(RoundedCorners(30))
                .into(holder.recipeImg)
        }
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<FavoritesInfo>) {
        items = newItems
        notifyDataSetChanged()
    }
}
