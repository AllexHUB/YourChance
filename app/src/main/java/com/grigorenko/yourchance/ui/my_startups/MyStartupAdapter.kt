package com.grigorenko.yourchance.ui.my_startups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.grigorenko.yourchance.R
import com.grigorenko.yourchance.database.model.Startup
import com.grigorenko.yourchance.databinding.MyStartupItemBinding
import com.squareup.picasso.Picasso


class MyStartupAdapter(
    private val listOfStartups: List<Startup>,
    private val myStartupClickListener: MyStartupClickListener
) : RecyclerView.Adapter<MyStartupViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyStartupViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MyStartupItemBinding.inflate(inflater, parent, false)
        return MyStartupViewHolder(binding, myStartupClickListener)
    }

    override fun onBindViewHolder(holder: MyStartupViewHolder, position: Int) {
        holder.bindStartup(listOfStartups[position])
    }

    override fun getItemCount() = listOfStartups.size
}

class MyStartupViewHolder(
    private val myStartupItemBinding: MyStartupItemBinding,
    private val myStartupClickListener: MyStartupClickListener
) : RecyclerView.ViewHolder(myStartupItemBinding.root) {

    fun bindStartup(startup: Startup) {
        Picasso.get()
            .load(startup.image.uri.toUri())
            .fit()
            .into(myStartupItemBinding.image)
        myStartupItemBinding.apply {
            name.text = startup.name
            category.text = startup.category
            iconCategory.setImageResource(getCategoryDrawableId(startup.category))
            wholeSumInvest.text = startup.moneyInvest.wholeSum.toString()
            collectedSumInvest.text = startup.moneyInvest.collectedSum.toString()
            location.text = startup.location.country + ", " + startup.location.city
            progressBarMoneyInvest.max = startup.moneyInvest.wholeSum.toInt()
            progressBarMoneyInvest.progress = startup.moneyInvest.collectedSum.toInt()
            amountOfViews.text = startup.views.toString()
            buttonDeleteStartup.setOnClickListener {
                myStartupClickListener.onDeleteStartup(startup)
            }
            buttonEditStartup.setOnClickListener {
                myStartupClickListener.onEditStartup(startup)
            }
        }
    }

    private fun getCategoryDrawableId(category: String) : Int {
        return when(category) {
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
