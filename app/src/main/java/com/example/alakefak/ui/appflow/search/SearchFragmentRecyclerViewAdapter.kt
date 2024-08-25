package com.example.alakefak.ui.appflow.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.alakefak.R
import com.example.alakefak.data.source.local.model.SearchResult

class SearchFragmentRecyclerViewAdapter(private var items: List<SearchResult>) :
    RecyclerView.Adapter<SearchFragmentRecyclerViewAdapter.SearchFragmentRecyclerViewViewHolder>() {

    private lateinit var myLister: Communicator

    interface Communicator {
        fun onItemClicked(position: Int)
    }

    fun setCommunicator(listener: Communicator) {
        myLister = listener
    }

    inner class SearchFragmentRecyclerViewViewHolder(private val row: View, listener: Communicator) : RecyclerView.ViewHolder(row) {
        val recipeName = row.findViewById<TextView>(R.id.searchRsultName)
        val recipeCategory = row.findViewById<TextView>(R.id.searchResultCategory)
        val recipeImageView = row.findViewById<ImageView>(R.id.searchResultImageView)
        val recipeArea = row.findViewById<TextView>(R.id.searchResultArea)

        init {
            itemView.setOnClickListener {
                listener.onItemClicked(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFragmentRecyclerViewViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.search_result_layout, parent, false)
        return SearchFragmentRecyclerViewViewHolder(row, myLister)
    }

    override fun onBindViewHolder(holder: SearchFragmentRecyclerViewViewHolder, position: Int) {
        val item = items.getOrNull(position)
        if (item != null) {
            holder.recipeName.text = item.name
            holder.recipeCategory.text = item.category
            holder.recipeArea.text = item.area
            Glide.with(holder.itemView.context)
                .load(item.imageUrl)
                .transform(RoundedCorners(30))
                .into(holder.recipeImageView)
        }
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<SearchResult>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun getItem(position: Int): SearchResult {
        return items[position]
    }
}