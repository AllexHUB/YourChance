package com.grigorenko.yourchance.ui.startuper.list_of_startups.tablayout.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grigorenko.yourchance.R
import com.grigorenko.yourchance.databinding.StartupItemBinding
import com.grigorenko.yourchance.domain.model.Startup
import com.grigorenko.yourchance.ui.startuper.list_of_startups.interfaces.StartupClickListener
import com.squareup.picasso.Picasso

class ListOfStartupsAdapter(
    private val startupClickListener: StartupClickListener,
    private val favoriteStartups: List<Startup>?,
    private val userUID: String,
    private val userType: String
) : ListAdapter<Startup, ListOfStartupsViewHolder>(StartupDiff) {
    companion object {
        private object StartupDiff : DiffUtil.ItemCallback<Startup>() {
            override fun areItemsTheSame(oldItem: Startup, newItem: Startup): Boolean {
                return oldItem.image == newItem.image
            }
            override fun areContentsTheSame(oldItem: Startup, newItem: Startup): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfStartupsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StartupItemBinding.inflate(inflater, parent, false)
        return ListOfStartupsViewHolder(binding, startupClickListener)
    }

    override fun onBindViewHolder(holder: ListOfStartupsViewHolder, position: Int) {
        val startup = getItem(position)
        holder.bindStartup(startup, favoriteStartups, userUID, userType)
    }

    override fun submitList(list: List<Startup>?) {
        super.submitList(list?.let {
            ArrayList(it)
        })
    }
}

class ListOfStartupsViewHolder(
    private val startupItemBinding: StartupItemBinding,
    private val startupClickListener: StartupClickListener
) : RecyclerView.ViewHolder(startupItemBinding.root) {
    fun bindStartup(startup: Startup, favoriteStartups: List<Startup>?, userUID: String, userType: String) {
        var isFavoriteStartup = false
        var isOwnStartup = false
        var isUserStartuper = false
        Picasso.get()
            .load(startup.image.uri.toUri())
            .fit().centerCrop()
            .placeholder(R.drawable.ic_loading_image)
            .into(startupItemBinding.image)
        startupItemBinding.apply {
            name.text = startup.name
            description.text = startup.description
            category.text = startup.category
            iconCategory.setImageResource(getCategoryDrawableId(startup.category))
            wholeSumInvest.text = startup.moneyInvest.wholeSum.toString()
            collectedSumInvest.text = startup.moneyInvest.collectedSum.toString()
            val tLocation = startup.location.country + ", " + startup.location.city
            location.text = tLocation
            progressBarMoneyInvest.max = startup.moneyInvest.wholeSum.toInt()
            progressBarMoneyInvest.progress = startup.moneyInvest.collectedSum.toInt()
            amountOfViews.text = startup.views.toString()
            if (userType == "Investor") {
                startupCardView.setCardBackgroundColor(Color.WHITE)
                if (favoriteStartups?.contains(startup) == true) {
                    iconFavorite.setImageResource(R.drawable.ic_remove_from_favorites_black)
                    isFavoriteStartup = true
                } else
                    iconFavorite.setImageResource(R.drawable.ic_add_to_favorites_black)
            } else {
                isUserStartuper = true
                if (userUID == startup.ownerId) {
                    isOwnStartup = true
                } else
                    startupCardView.setCardBackgroundColor(Color.WHITE)
            }
            startupCardView.setOnClickListener {
                startupClickListener.onClick(startup, isFavoriteStartup, isOwnStartup, isUserStartuper)
            }
        }
    }

    private fun getCategoryDrawableId(category: String): Int {
        return when (category) {
            "Бизнес" -> R.drawable.ic_business_category_black_24dp
            "Еда" -> R.drawable.ic_food_category_black_24dp
            "Искусство" -> R.drawable.ic_art_category_black_24dp
            "Общество" -> R.drawable.ic_society_category_black_24dp
            "IT" -> R.drawable.ic_it_category_black_24dp
            "Технологии" -> R.drawable.ic_techn_category_black_24dp
            "Путешествия" -> R.drawable.ic_trip_category_black_24dp
            "Спорт" -> R.drawable.ic_sport_category_black_24dp
            else -> R.drawable.ic_business_category_black_24dp
        }
    }
}
