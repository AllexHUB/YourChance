package com.grigorenko.yourchance.ui.investor.favorite_startups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.grigorenko.yourchance.databinding.FragmentFavoriteStartupsBinding
import com.grigorenko.yourchance.domain.model.Startup
import com.grigorenko.yourchance.domain.viewmodel.StartupViewModel

class FavoriteStartupsFragment : Fragment(), StartupClickListener {
    private var _binding: FragmentFavoriteStartupsBinding? = null
    private val binding get() = _binding!!

    private val startupViewModel: StartupViewModel by activityViewModels()

    private val adapter = FavoriteStartupsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteStartupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startupViewModel.apply {
            userStartups.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
        binding.favStartupsRecyclerView.adapter = adapter
        binding.favStartupsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(startup: Startup, isFavoriteStartup: Boolean) {
        findNavController().navigate(
            FavoriteStartupsFragmentDirections.actionFavoriteStartupsToSelectedStartup(
                startup,
                isFavoriteStartup,
                false,
                false
            )
        )
    }
}