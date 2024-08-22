package com.example.alakefak.ui.appflow.home


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.alakefak.R

class CategoriesAdapter(private var items: List<String>, private val viewModel: HomeViewModel) : RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
    private var prevBtn: Button? = null

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
                holder.categoryBtn.alpha = 0.7F
            } else {
                holder.categoryBtn.alpha = 1F
            }

            holder.categoryBtn.setOnClickListener {
                if (!viewModel.clickState) {
                    if (viewModel.selectedFilter != item) {
                        viewModel.selectedFilter = item
                        holder.categoryBtn.alpha = 1F
                    } else {
                        viewModel.selectedFilter = HomeViewModel.NO_FILTER
                        holder.categoryBtn.alpha = 0.7F
                    }
                    viewModel.getFilteredItems()
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }

}