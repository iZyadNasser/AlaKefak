package com.example.alakefak.ui.appflow.home


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.alakefak.R
import com.example.alakefak.ui.appflow.RecipeActivity.Companion.lastPressedButton

class CategoriesAdapter(private var items: List<String>, private val viewModel: HomeViewModel) :
    RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
    class MyViewHolder(private val col: View) : RecyclerView.ViewHolder(col) {
        var categoryBtn = col.findViewById<Button>(R.id.categoryBtn)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val col = inflater.inflate(R.layout.my_col_item, parent, false)
        return MyViewHolder(col)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items.getOrNull(position)
        if (item != null) {
            holder.categoryBtn.text = item
            if (holder.categoryBtn == lastPressedButton) {
                holder.categoryBtn.setBackgroundResource(R.drawable.rounded_button)
            }
            if (item != viewModel.selectedFilter) {
                holder.categoryBtn.setBackgroundResource(R.drawable.rounded_button)
            } else {
                holder.categoryBtn.setBackgroundResource(R.drawable.rounded_button_active)
            }

            holder.categoryBtn.setOnClickListener {
                if (!HomeViewModel.currentlyLoading) {
                    holder.categoryBtn.setBackgroundResource(R.drawable.rounded_button)
                    notifyDataSetChanged()
                    if (viewModel.selectedFilter != item) {
                        viewModel.selectedFilter = item
                        holder.categoryBtn.setBackgroundResource(R.drawable.rounded_button_active)
                        lastPressedButton = holder.categoryBtn
                    } else {
                        viewModel.selectedFilter = HomeViewModel.NO_FILTER
                        holder.categoryBtn.setBackgroundResource(R.drawable.rounded_button)
                        lastPressedButton = null
                    }
                    viewModel.getFilteredItems()
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }
}