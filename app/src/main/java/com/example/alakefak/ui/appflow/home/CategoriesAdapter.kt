package com.example.alakefak.ui.appflow.home


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.alakefak.R

class CategoriesAdapter(val items: List<String>) : RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
    class MyViewHolder(val col: View) : RecyclerView.ViewHolder(col) {
        var categoryBtn = col.findViewById<Button>(R.id.categoryBtn)
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
        }
    }

    override fun getItemCount(): Int = items.size

}