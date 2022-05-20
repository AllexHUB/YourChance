package com.grigorenko.yourchance.ui.startuper.list_of_startups.tablayout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grigorenko.yourchance.R
import com.grigorenko.yourchance.database.model.Startup
import com.grigorenko.yourchance.databinding.StartupItemBinding
import com.grigorenko.yourchance.ui.startuper.list_of_startups.interfaces.StartupClickListener
import com.squareup.picasso.Picasso

class ListOfStartupsAdapter(
    private val startupClickListener: StartupClickListener,
    private val favoriteStartups: List<Startup>?,
    private val owner: LifecycleOwner
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
        holder.bindStartup(startup, favoriteStartups, owner)
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

    private var isFavoriteStartup: Boolean = false

    fun bindStartup(startup: Startup, favoriteStartups: List<Startup>?, owner: LifecycleOwner) {
        val newImage = MutableLiveData(startupItemBinding.image)
        Picasso.get()
            .load(startup.image.uri.toUri())
            .fit()
            .into(startupItemBinding.image)
        startupItemBinding.apply {
            newImage.observe(owner) {
                progressBar.visibility = View.GONE
                image.visibility = View.VISIBLE
            }
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
//            if (!favoriteStartups.isNullOrEmpty())
//                for (i in favoriteStartups.iterator())
//                    if (i.image == startup.image) {
//                        iconFavorite.setImageResource(R.drawable.ic_remove_from_favorites_black)
//                        isFavoriteStartup = true
//                        break
//                    }
            if (!favoriteStartups.isNullOrEmpty())
                if (favoriteStartups.contains(startup)) {
                    iconFavorite.setImageResource(R.drawable.ic_remove_from_favorites_black)
                    isFavoriteStartup = true
                }
            startupCardView.setOnClickListener {
                startupClickListener.onClick(startup, isFavoriteStartup)
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
