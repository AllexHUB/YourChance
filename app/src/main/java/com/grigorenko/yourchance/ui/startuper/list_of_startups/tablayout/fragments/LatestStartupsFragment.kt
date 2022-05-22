package com.grigorenko.yourchance.ui.startuper.list_of_startups.tablayout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.grigorenko.yourchance.databinding.FragmentLatestStartupsBinding
import com.grigorenko.yourchance.domain.model.Startup
import com.grigorenko.yourchance.domain.model.User
import com.grigorenko.yourchance.domain.viewmodel.StartupViewModel
import com.grigorenko.yourchance.domain.viewmodel.UserViewModel
import com.grigorenko.yourchance.ui.startuper.list_of_startups.interfaces.StartupClickListener
import com.grigorenko.yourchance.ui.startuper.list_of_startups.tablayout.adapter.ListOfStartupsAdapter

class LatestStartupsFragment : Fragment(), StartupClickListener {
    private var _binding: FragmentLatestStartupsBinding? = null
    private val binding get() = _binding!!

    private val startupViewModel: StartupViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var adapter: ListOfStartupsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLatestStartupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = requireActivity().intent.getParcelableExtra<User>("user")!!
        val userUID = userViewModel.getCurrentUserUID()
        startupViewModel.apply {
            getStartupsOrderedByDate()
            userStartups.observe(viewLifecycleOwner) { favStartups ->
                adapter = if (favStartups != null)
                    ListOfStartupsAdapter(this@LatestStartupsFragment, favStartups, userUID, user.type)
                else
                    ListOfStartupsAdapter(this@LatestStartupsFragment, null, userUID, user.type)
                binding.recyclerView.adapter = adapter
                latestStartups.observe(viewLifecycleOwner) { allStartups ->
                    adapter.submitList(allStartups)
                }
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onClick(startup: Startup, isFavoriteStartup: Boolean, isOwnStartup: Boolean, isUserStartuper: Boolean) {
        findNavController().navigate(
            ListOfStartupsFragmentDirections.actionListOfStartupsToSelectedStartup(
                startup, isFavoriteStartup, isOwnStartup, isUserStartuper
            )
        )
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerView.smoothScrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}