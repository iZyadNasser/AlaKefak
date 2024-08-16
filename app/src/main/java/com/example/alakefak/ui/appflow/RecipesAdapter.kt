package com.example.alakefak.ui.appflow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipesAdapter(val myList: ArrayList<RecipeResponse>) :
    RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {
   private lateinit var myLister: Communicator
    interface Communicator {
        fun onItemClicked(position: Int)
    }

    fun setCommunicator(listner: Communicator) {
        myLister = listner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view, myLister)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val RecipeResponse = myList[position]
        Glide.with(holder.imageView.context).load(RecipeResponse.image).into(holder.imageView)
        holder.textViewOne.text = RecipeResponse.name

    }

    override fun getItemCount(): Int = myList.size

    class MyViewHolder(ItemView: View, listner: Communicator) : RecyclerView.ViewHolder(ItemView) {

        val imageView: ImageView = itemView.findViewById(R.id.img)
        val textViewOne: TextView = itemView.findViewById(R.id.txOne)

        init {
            itemView.setOnClickListener {
                listner.onItemClicked(adapterPosition)
            }
        }

    }

}
