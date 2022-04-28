package com.grigorenko.yourchance.ui.my_startups.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.grigorenko.yourchance.database.model.Startup
import com.grigorenko.yourchance.databinding.FragmentMyStartupBinding
import com.grigorenko.yourchance.ui.create_new_startup.StartupDetailsViewModel
import com.grigorenko.yourchance.ui.my_startups.MyStartupAdapter
import com.grigorenko.yourchance.ui.my_startups.MyStartupClickListener
import com.grigorenko.yourchance.database.viewmodel.StartupViewModel
import com.grigorenko.yourchance.database.viewmodel.UserViewModel

class MyStartupFragment : Fragment(), MyStartupClickListener {
    private var _binding: FragmentMyStartupBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private val startupViewModel: StartupViewModel by activityViewModels()
    private val sharedViewModel: StartupDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyStartupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userUID = userViewModel.getCurrentUserUID()
        startupViewModel.manageUserStartups(userUID)
        startupViewModel.userStartups.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.startupRecyclerView.layoutManager = LinearLayoutManager(context)
                binding.startupRecyclerView.adapter = MyStartupAdapter(it, this@MyStartupFragment)
            } else
                binding.startupRecyclerView.layoutManager = null
        }
        binding.fabAddNewStartup.setOnClickListener {
            findNavController().navigate(MyStartupFragmentDirections.actionNavMyStartupToCreateStartupNavigation())
        }
//        setHasOptionsMenu(true)
    }

    //    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//    }

    override fun onResume() {
        super.onResume()
        startupViewModel.isStartupUpdated.postValue(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDeleteStartup(startup: Startup) {
        val userUID = userViewModel.getCurrentUserUID()
        startupViewModel.deleteUserStartup(userUID, startup)
    }

    override fun onEditStartup(startup: Startup) {
        sharedViewModel.setDate(startup.date)
        findNavController().navigate(MyStartupFragmentDirections.actionNavMyStartupToEditStartupFragment(startup))
    }
}