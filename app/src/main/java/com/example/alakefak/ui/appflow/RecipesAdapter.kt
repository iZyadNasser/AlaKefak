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
import com.example.alakefak.data.source.remote.model.ApiResponse
import com.example.alakefak.data.source.remote.model.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

class RecipesAdapter(val myList: ArrayList<Meal>) :
    RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {
   private lateinit var myLister: Communicator
    private lateinit var recipeImageView:TextView
    private var isFavourite:Boolean=true

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
        val meal = myList[position]
        holder.bind(meal)

        holder.heartBtn.setOnClickListener {
            myLister.onItemClicked(position)
            toggleHeart(holder.heartBtn)
        }
    }

    override fun getItemCount(): Int = myList.size


    private fun toggleHeart(heartBtn: ImageButton) {
        val isSelected = heartBtn.tag == "selected"
        if (isSelected) {
            heartBtn.setImageResource(R.drawable.ic_heart_outline)
            heartBtn.tag = "unselected"
        } else {
            heartBtn.setImageResource(R.drawable.ic_heart_filled)
            heartBtn.tag = "selected"
        }
        animateHeart(heartBtn)
    }

    private fun animateHeart(heartBtn: ImageButton) {
        val scaleX = ObjectAnimator.ofFloat(heartBtn, "scaleX", 0.8f, 1.2f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(heartBtn, "scaleY", 0.8f, 1.2f, 1.0f)
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            duration = 300
            start()
        }
    }

    class MyViewHolder(ItemView: View, listner: Communicator) : RecyclerView.ViewHolder(ItemView) {

        val recipeImageView: ImageView = itemView.findViewById(R.id.recipeImage)
        val recipeTextView: TextView = itemView.findViewById(R.id.recipeTx)
        val heartBtn: ImageButton = itemView.findViewById(R.id.btnHeart)

        fun bind(meal: Meal) {
            recipeTextView.text = meal.strMeal
            Glide.with(recipeImageView.context).load(meal.strImageSource).into(recipeImageView)
        }


        init {
            itemView.setOnClickListener {
                listner.onItemClicked(adapterPosition)
            }
        }

    }

}
