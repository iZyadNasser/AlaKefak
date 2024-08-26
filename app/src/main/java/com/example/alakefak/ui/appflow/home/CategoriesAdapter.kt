package com.example.alakefak.ui.appflow.home


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.alakefak.R

class CategoriesAdapter(private var items: List<String>, private val viewModel: HomeViewModel) : RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
    private var viewHolder: Button? = null

    class MyViewHolder(private val col: View) : RecyclerView.ViewHolder(col) {
        var categoryBtn = col.findViewById<Button>(R.id.categoryBtn)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val col = inflater.inflate(R.layout.my_col_item, parent, false)
        return MyViewHolder(col)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items.getOrNull(position)
        if (item != null) {
            holder.categoryBtn.text = item
            if (item != viewModel.selectedFilter) {
                holder.categoryBtn.setBackgroundColor(Color.parseColor("#8E8989"))
                viewHolder = null
            } else {
                holder.categoryBtn.alpha = 1F
                viewHolder = holder.categoryBtn
            }

            holder.categoryBtn.setOnClickListener {
                viewHolder?.setBackgroundColor(Color.parseColor("#8E8989"))
                if (viewModel.selectedFilter != item) {
                    viewModel.selectedFilter = item
                    holder.categoryBtn.alpha = 1F
                    viewHolder = holder.categoryBtn
                } else {
                    viewModel.selectedFilter = HomeViewModel.NO_FILTER
                    holder.categoryBtn.setBackgroundColor(Color.parseColor("#8E8989"))
                    viewHolder = null
                }
                viewModel.getFilteredItems()
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }

}