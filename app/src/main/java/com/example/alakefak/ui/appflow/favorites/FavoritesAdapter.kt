package com.example.alakefak.ui.appflow.favorites

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.alakefak.R
import com.example.alakefak.data.repository.FavoriteRepository
import com.example.alakefak.data.source.local.database.FavoritesDatabaseDao
import com.example.alakefak.data.source.local.model.FavoritesInfo
import com.example.alakefak.data.source.remote.model.Meal
import com.example.alakefak.ui.appflow.RecipeActivity
import com.example.alakefak.ui.appflow.home.RecipesAdapter.MyViewHolder
import com.example.alakefak.ui.authflow.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesAdapter(
    var items: List<FavoritesInfo>,
    private val favoritesDao: FavoritesDatabaseDao
) :
    RecyclerView.Adapter<FavoritesAdapter.MyViewHolder>() {
    private val repo = FavoriteRepository(favoritesDao)

    private lateinit var myLister: Communicator

    interface Communicator {
        fun onItemClicked(position: Int)
    }

    fun setCommunicator(listener: Communicator) {
        myLister = listener
    }

    class MyViewHolder(private val row: View, listener: Communicator) : RecyclerView.ViewHolder(row) {
        var recipeName: TextView = row.findViewById(R.id.nameTextView)
        var recipeCategory: TextView = row.findViewById(R.id.categoryTextView)
        val recipeArea : TextView = row.findViewById(R.id.areaTextView)
        var recipeImg: ImageView = row.findViewById(R.id.recipeFavImageView)
        val heartBtn: ImageButton = itemView.findViewById(R.id.btnHeartFav)
        init {
            itemView.setOnClickListener {
                listener.onItemClicked(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.my_fav_item, parent, false)
        return MyViewHolder(row,myLister)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items.getOrNull(position)
        if (item != null) {
            holder.recipeName.text = item.recipeName
            holder.recipeCategory.text = item.recipeCategory
            holder.recipeArea.text = item.recipeArea
            holder.heartBtn.setImageResource(R.drawable.ic_heart_filled)
            Glide.with(holder.itemView.context)
                .load(item.recipeImg)
                .transform(RoundedCorners(30))
                .placeholder(R.drawable.placeholder)
                .into(holder.recipeImg)

            holder.heartBtn.setOnClickListener {
                showFavoriteConfirmationDialogue(holder, item, holder.itemView.context)
            }

        }
    }

    private fun showFavoriteConfirmationDialogue(
        holder: MyViewHolder,
        item: FavoritesInfo,
        context: Context
    ) {
        Utils.showSignOutDialog(
            context = context,
            title = context.getString(R.string.remove_item_from_favorites),
            iconId = R.drawable.trash_bin_trash,
            message = context.getString(R.string.remove_favorites_confirmation),
            positiveButtonText = context.getString(R.string.remove),
            negativeButtonText = context.getString(R.string.cancel)
        ) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
            holder.itemView.startAnimation(animation)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    handleFavoriteBtn(holder, item)
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
    }

    private fun handleFavoriteBtn(
        holder: MyViewHolder,
        item: FavoritesInfo
    ) {
        holder.heartBtn.setImageResource(R.drawable.ic_heart_outline)
        CoroutineScope(Dispatchers.IO).launch {
            repo.deleteFavorite(item)
            withContext(Dispatchers.Main) {
                setupItems(repo.getAllFavorites(RecipeActivity.curUser?.id!!))
            }
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setupItems(newItems: List<FavoritesInfo>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun getItem(position: Int): FavoritesInfo {
        return items[position]
    }
}