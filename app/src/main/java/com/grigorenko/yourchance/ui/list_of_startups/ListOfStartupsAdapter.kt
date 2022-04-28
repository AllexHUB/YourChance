package com.grigorenko.yourchance.ui.list_of_startups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.grigorenko.yourchance.R
import com.grigorenko.yourchance.database.model.Startup
import com.grigorenko.yourchance.databinding.StartupItemBinding
import com.squareup.picasso.Picasso

class ListOfStartupsAdapter(
    private val sortedStartups: List<Startup>,
    private val startupClickListener: StartupClickListener
) : RecyclerView.Adapter<ListOfStartupsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfStartupsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StartupItemBinding.inflate(inflater, parent, false)
        return ListOfStartupsViewHolder(binding, startupClickListener)
    }

    override fun onBindViewHolder(holder: ListOfStartupsViewHolder, position: Int) {
        holder.bindStartup(sortedStartups[position])
    }

    override fun getItemCount() = sortedStartups.size
}

class ListOfStartupsViewHolder(
    private val startupItemBinding: StartupItemBinding,
    private val startupClickListener: StartupClickListener
) : RecyclerView.ViewHolder(startupItemBinding.root) {

    fun bindStartup(startup: Startup) {
        Picasso.get()
            .load(startup.image.uri.toUri())
            .fit()
            .into(startupItemBinding.image)
        startupItemBinding.apply {
            name.text = startup.name
            description.text = startup.description
            category.text = startup.category
            iconCategory.setImageResource(getCategoryDrawableId(startup.category))
            wholeSumInvest.text = startup.moneyInvest.wholeSum.toString()
            collectedSumInvest.text = startup.moneyInvest.collectedSum.toString()
            location.text = startup.location.country + ", " + startup.location.city
            progressBarMoneyInvest.max = startup.moneyInvest.wholeSum.toInt()
            progressBarMoneyInvest.progress = startup.moneyInvest.collectedSum.toInt()
            amountOfViews.text = startup.views.toString()
            startupCardView.setOnClickListener {
                startupClickListener.onClick(startup)
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
