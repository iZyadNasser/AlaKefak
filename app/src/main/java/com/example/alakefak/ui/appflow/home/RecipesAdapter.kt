package com.example.alakefak.ui.appflow.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alakefak.R
import com.example.alakefak.data.repository.FavoriteRepository
import com.example.alakefak.data.source.local.database.FavoritesDatabaseDao
import com.example.alakefak.data.source.local.model.FavoritesInfo
import com.example.alakefak.data.source.remote.model.Meal
import com.example.alakefak.ui.appflow.RecipeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipesAdapter(
    private var myList: List<Meal>,
    private val favoritesDao: FavoritesDatabaseDao
) :
    RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private val repo = FavoriteRepository(favoritesDao)
    lateinit var myLister: Communicator

    interface Communicator {
        fun onItemClicked(position: Int)
    }

    fun setCommunicator(listner: Communicator) {
        myLister = listner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(view,myLister)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = myList.getOrNull(position)
        if (item != null) {
            Glide.with(holder.recipeImageView.context).load(item.strMealThumb)
                .into(holder.recipeImageView)

            holder.recipeNameTextView.text = item.strMeal

            // TODO(Use isFavorite flag)

            CoroutineScope(Dispatchers.IO).launch {
                val favoritesInfo = repo.findItem(item.idMeal ?: "", RecipeActivity.curUser?.id!!)

                withContext(Dispatchers.Main) {
                    if (favoritesInfo != null) {
                        holder.heartBtn.setImageResource(R.drawable.ic_heart_filled)
                    } else {
                        holder.heartBtn.setImageResource(R.drawable.ic_heart_outline)
                    }
                }
            }

            holder.heartBtn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val favoritesInfo =
                        repo.findItem(item.idMeal ?: "", RecipeActivity.curUser?.id!!)
                    if (favoritesInfo != null) {
                        withContext(Dispatchers.Main) {
                            holder.heartBtn.setImageResource(R.drawable.ic_heart_outline)
                        }
                        repo.deleteFavorite(favoritesInfo)
                    } else {
                        withContext(Dispatchers.Main) {
                            holder.heartBtn.setImageResource(R.drawable.ic_heart_filled)
                        }
                        val newFav = FavoritesInfo(
                            id = item.idMeal ?: "",
                            recipeName = item.strMeal ?: "",
                            recipeCategory = item.strCategory ?: "",
                            recipeImg = item.strMealThumb ?: "",
                            recipeArea = item.strArea ?: "",
                            userId = RecipeActivity.curUser?.id!!
                        )
                        repo.insertFavorite(newFav)
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

    fun updateItems(newItems: List<Meal>) {
        myList = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = myList.size

    inner class MyViewHolder(itemView: View,listner: Communicator) : RecyclerView.ViewHolder(itemView) {
        val recipeImageView: ImageView = this.itemView.findViewById(R.id.recipeImage)
        val recipeNameTextView: TextView = this.itemView.findViewById(R.id.recipeName)
        val heartBtn: ImageButton = this.itemView.findViewById(R.id.btnHeart)
        init {
            itemView.setOnClickListener {
                listner.onItemClicked(adapterPosition)
            }
        }

    }
    fun getItem(position: Int): Meal {
        return myList[position]
    }

}