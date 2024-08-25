package com.example.alakefak.ui.appflow.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.example.alakefak.R
import com.example.alakefak.data.source.remote.model.Meal

class RecipesAdapter(
    private var myList: List<Meal>,
    private val viewModel: HomeViewModel
) :
    RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private lateinit var myLister: Communicator

    interface Communicator {
        fun onItemClicked(position: Int)
    }

    fun setCommunicator(listener: Communicator) {
        myLister = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(view,myLister)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = myList.getOrNull(position)
        if (item != null) {
            Glide.with(holder.recipeImageView.context)
                .load(item.strMealThumb)
                .placeholder(R.drawable.placeholder)
                .into(holder.recipeImageView)

            holder.recipeNameTextView.text = item.strMeal

            if (item.isFavorite) {
                holder.heartBtn.setImageResource(R.drawable.ic_heart_filled)
            } else {
                holder.heartBtn.setImageResource(R.drawable.ic_heart_outline)
            }

            holder.heartBtn.setOnClickListener {
                if (item.isFavorite) {
                    showFavoriteConfirmationDialogue(holder,item,holder.itemView.context)

                } else {
                    item.isFavorite = true
                    holder.heartBtn.setImageResource(R.drawable.ic_heart_filled)
                    viewModel.insertFav(item)
                }

            }
        }
    }

    private fun showFavoriteConfirmationDialogue(
        holder: MyViewHolder,
        item: Meal,
        context: Context
    ) {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle("Remove item from favorites")
            setMessage(context.getString(R.string.remove_favorites_confirmation))
            setPositiveButton(context.getString(R.string.remove)) { dialog, _ ->
                Toast.makeText(context,
                    context.getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                item.isFavorite = false
                holder.heartBtn.setImageResource(R.drawable.ic_heart_outline)
                viewModel.deleteFav(item)
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            show()
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