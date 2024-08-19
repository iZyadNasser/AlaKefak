package com.example.alakefak.ui.appflow.search

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.alakefak.R
import com.example.alakefak.data.source.local.model.SearchResult

class SearchFragmentRecyclerViewAdapter(private var items: List<SearchResult>) :
    RecyclerView.Adapter<SearchFragmentRecyclerViewAdapter.SearchFragmentRecyclerViewViewHolder>() {
    class SearchFragmentRecyclerViewViewHolder(private val row: View) : RecyclerView.ViewHolder(row) {
        val recipeName = row.findViewById(R.id.)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFragmentRecyclerViewViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.search_result_layout, parent, false)
        return SearchFragmentRecyclerViewViewHolder(row)
    }

    override fun onBindViewHolder(holder: SearchFragmentRecyclerViewViewHolder, position: Int) {
        val item = items.getOrNull(position)
        if (item != null) {
            holder.recipeName.text = item.recipeName
            holder.recipeCategory.text = item.recipeCategory
            Glide.with(holder.itemView.context)
                .load(item.recipeImg)
                .transform(RoundedCorners(30))
                .into(holder.recipeImg)

            holder.heartBtn.setOnClickListener {
                val isSelected = holder.heartBtn.tag == "selected"
                if (isSelected) {
                    holder.heartBtn.setImageResource(R.drawable.ic_heart_outline)
                    holder.heartBtn.tag = "unselected"
                } else {
                    holder.heartBtn.setImageResource(R.drawable.ic_heart_filled)
                    holder.heartBtn.tag = "selected"
                }
                val scaleX = ObjectAnimator.ofFloat(holder.heartBtn, "scaleX", 0.8f, 1.2f, 1.0f)
                val scaleY = ObjectAnimator.ofFloat(holder.heartBtn, "scaleY", 0.8f, 1.2f, 1.0f)
                AnimatorSet().apply {
                    playTogether(scaleX, scaleY)
                    duration = 300
                    start()
                }

            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<SearchResult>) {
        items = newItems
        notifyDataSetChanged()
    }
}