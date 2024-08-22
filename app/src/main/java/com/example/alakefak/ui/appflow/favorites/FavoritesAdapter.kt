package com.example.alakefak.ui.appflow.favorites

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        var recipeName: TextView = row.findViewById(R.id.nameTextView)
        var recipeCategory: TextView = row.findViewById(R.id.categoryTextView)
        val recipeArea : TextView = row.findViewById(R.id.areaTextView)
        var recipeImg: ImageView = row.findViewById(R.id.recipeFavImageView)
        val heartBtn: ImageButton = itemView.findViewById(R.id.btnHeartFav)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.my_fav_item, parent, false)
        return MyViewHolder(row)
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
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setMessage("Are you sure you want to remove this item from favorites?")
            setPositiveButton("Remove") { dialog, _ ->
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                handleFavoriteBtn(holder, item)
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            show()
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
                setupItems(repo.getAllFavorites())
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun setupItems(newItems: List<FavoritesInfo>) {
        items = newItems
        notifyDataSetChanged()
    }
}