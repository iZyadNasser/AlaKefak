package com.example.alakefak.ui.appflow.home

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
import com.example.alakefak.data.repository.FavoriteRepository
import com.example.alakefak.data.source.local.database.FavoritesDatabaseDao
import com.example.alakefak.data.source.local.model.FavoritesInfo
import com.example.alakefak.data.source.remote.model.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RecipesAdapter(
    val myList: List<FavoritesInfo>,
    private val favoritesDao: FavoritesDatabaseDao
) :
    RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {
    private lateinit var myLister: Communicator
    private val repo = FavoriteRepository(favoritesDao)

    interface Communicator {
        fun onItemClicked(position: Int)
    }

    fun setCommunicator(listner: Communicator) {
        myLister = listner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(view, myLister)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val meal = bindRecipeInfo(position, holder)

        checkFavExistence(meal, holder)

        changeHeartState(meal, holder)
    }


    private fun bindRecipeInfo(
        position: Int,
        holder: MyViewHolder
    ): FavoritesInfo {
        val meal = myList[position]
        Glide.with(holder.recipeImageView.context).load(meal.recipeImg).into(holder.recipeImageView)
        holder.recipeTextView.text = meal.recipeName
        return meal
    }


    private fun checkFavExistence(
        meal: FavoritesInfo,
        holder: MyViewHolder
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            if (repo.findItem(meal.id) != null) {
                holder.heartBtn.setImageResource(R.drawable.ic_heart_filled)
            } else {
                holder.heartBtn.setImageResource(R.drawable.ic_heart_outline)
            }
        }
    }


    private fun changeHeartState(
        meal: FavoritesInfo,
        holder: MyViewHolder
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            if (repo.findItem(meal.id) != null) {
                holder.heartBtn.setOnClickListener {
                    holder.heartBtn.setImageResource(R.drawable.ic_heart_outline)
                    animateHeart(holder)
                    CoroutineScope(Dispatchers.IO).launch {
                        repo.deleteFavorite(meal)
                    }
                }
            } else {
                holder.heartBtn.setOnClickListener {
                    holder.heartBtn.setImageResource(R.drawable.ic_heart_filled)
                    animateHeart(holder)
                    CoroutineScope(Dispatchers.IO).launch {
                        repo.insertFavorite(meal)
                    }
                }
            }
        }
    }


    private fun animateHeart(holder: MyViewHolder) {
        val scaleX = ObjectAnimator.ofFloat(holder.heartBtn, "scaleX", 0.8f, 1.2f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(holder.heartBtn, "scaleY", 0.8f, 1.2f, 1.0f)
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            duration = 300
            start()
        }
    }

    override fun getItemCount(): Int = myList.size
    class MyViewHolder(ItemView: View, listner: Communicator) : RecyclerView.ViewHolder(ItemView) {
        val recipeImageView: ImageView = itemView.findViewById(R.id.recipeImage)
        val recipeTextView: TextView = itemView.findViewById(R.id.recipeTx)
        val heartBtn: ImageButton = itemView.findViewById(R.id.btnHeart)

        init {
            itemView.setOnClickListener {
                listner.onItemClicked(adapterPosition)
            }
        }

    }

}