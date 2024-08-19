package com.example.alakefak.ui.appflow

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alakefak.R

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(view, myLister)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val RecipeResponse = myList[position]
        Glide.with(holder.imageView.context).load(RecipeResponse.image).into(holder.imageView)
        holder.textViewOne.text = RecipeResponse.name

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

    override fun getItemCount(): Int = myList.size

    class MyViewHolder(ItemView: View, listner: Communicator) : RecyclerView.ViewHolder(ItemView) {

        val imageView: ImageView = itemView.findViewById(R.id.img)
        val textViewOne: TextView = itemView.findViewById(R.id.txOne)
        val heartBtn: ImageButton = itemView.findViewById(R.id.btnHeart)

        init {
            itemView.setOnClickListener {
                listner.onItemClicked(adapterPosition)
            }
        }

    }

}
