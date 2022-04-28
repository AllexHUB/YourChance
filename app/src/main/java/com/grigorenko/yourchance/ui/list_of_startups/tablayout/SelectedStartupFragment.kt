package com.grigorenko.yourchance.ui.list_of_startups.tablayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.grigorenko.yourchance.R
import com.grigorenko.yourchance.databinding.FragmentSelectedStartupBinding
import com.grigorenko.yourchance.ui.MainActivity
import com.squareup.picasso.Picasso


class SelectedStartupFragment : Fragment() {
    private var _binding: FragmentSelectedStartupBinding? = null
    private val binding get() = _binding!!

    private val args: SelectedStartupFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedStartupBinding.inflate(inflater, container, false)
        (activity as MainActivity).setDrawerLocked()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            Picasso.get()
                .load(args.startup.image.uri.toUri())
                .fit()
                .into(image)
            name.text = args.startup.name
            description.text = args.startup.description
            category.text = args.startup.category
            iconCategory.setImageResource(getCategoryDrawableId(args.startup.category))
            wholeSumInvest.text = args.startup.moneyInvest.wholeSum.toString()
            collectedSumInvest.text = args.startup.moneyInvest.collectedSum.toString()
            location.text = getString(
                R.string.set_location,
                args.startup.location.country,
                args.startup.location.city
            )
            amountOfViews.text = args.startup.views.toString()
            progressBarMoneyInvest.max = args.startup.moneyInvest.wholeSum.toInt()
            progressBarMoneyInvest.progress = args.startup.moneyInvest.collectedSum.toInt()
            buttonWriteToAuthor.setOnClickListener {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}