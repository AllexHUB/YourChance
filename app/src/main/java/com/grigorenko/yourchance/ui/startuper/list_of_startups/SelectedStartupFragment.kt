package com.grigorenko.yourchance.ui.startuper.list_of_startups

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.grigorenko.yourchance.R
import com.grigorenko.yourchance.databinding.FragmentSelectedStartupBinding
import com.grigorenko.yourchance.domain.viewmodel.StartupViewModel
import com.grigorenko.yourchance.domain.viewmodel.UserViewModel
import com.grigorenko.yourchance.ui.MainActivity
import com.squareup.picasso.Picasso


class SelectedStartupFragment : Fragment() {
    private var _binding: FragmentSelectedStartupBinding? = null
    private val binding get() = _binding!!

    private val args: SelectedStartupFragmentArgs by navArgs()
    private var isFavoriteStartup = false

    private val startupViewModel: StartupViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedStartupBinding.inflate(inflater, container, false)
        (activity as MainActivity).setDrawerLocked()
        setHasOptionsMenu(true)
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
            isFavoriteStartup = args.isFavoriteStartup
            if (args.isOwnStartup)
                buttonWriteToAuthor.visibility = View.GONE
            buttonWriteToAuthor.setOnClickListener {
                userViewModel.apply {
                    userModel.value = null
                    getUserByUID(args.startup.ownerId)
                    userModel.observe(viewLifecycleOwner) { companion ->
                        if (companion != null)
                            findNavController().navigate(
                                SelectedStartupFragmentDirections.actionSelectedStartupToChatFragment(
                                    userViewModel.getCurrentUserUID(),
                                    args.startup.ownerId,
                                    companion
                                )
                            )
                    }
                }
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

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!args.isUserStartuper) {
            menuInflater.inflate(R.menu.curr_startup_menu, menu)
            if (isFavoriteStartup)
                menu.findItem(R.id.set_as_favorite).icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_remove_from_favorites)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.set_as_favorite) {
            val userUID = userViewModel.getCurrentUserUID()
            if (isFavoriteStartup) {
                startupViewModel.deleteStartupFromFavorites(userUID, args.startup)
                item.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_add_to_favorites)
                isFavoriteStartup = false
            } else {
                startupViewModel.addStartupToFavorites(userUID, args.startup.image)
                item.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_remove_from_favorites)
                isFavoriteStartup = true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).setDrawerUnlocked()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}