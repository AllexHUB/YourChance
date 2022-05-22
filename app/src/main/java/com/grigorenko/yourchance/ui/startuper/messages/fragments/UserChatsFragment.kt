package com.grigorenko.yourchance.ui.startuper.messages.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.grigorenko.yourchance.databinding.FragmentUserChatsBinding
import com.grigorenko.yourchance.domain.model.User
import com.grigorenko.yourchance.domain.viewmodel.ChatViewModel
import com.grigorenko.yourchance.domain.viewmodel.UserViewModel
import com.grigorenko.yourchance.ui.startuper.messages.ChatClickListener
import com.grigorenko.yourchance.ui.startuper.messages.adapters.UserChatsAdapter

class UserChatsFragment : Fragment(), ChatClickListener {
    private var _binding: FragmentUserChatsBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private val chatViewModel: ChatViewModel by activityViewModels()

    private val adapter = UserChatsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.userChatsRecyclerView.adapter = adapter
        binding.userChatsRecyclerView.layoutManager = LinearLayoutManager(context)
        chatViewModel.userChatsPresentation.observe(viewLifecycleOwner) {
            binding.userChatsRecyclerView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            adapter.submitList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        val userUID = userViewModel.getCurrentUserUID()
        chatViewModel.getUserChats(userUID)
    }

    override fun onPause() {
        super.onPause()
        binding.userChatsRecyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(firstUserId: String, secondUserId: String, companion: User) {
        findNavController().navigate(
            UserChatsFragmentDirections.actionMessagesToChatFragment(
                firstUserId,
                secondUserId,
                companion
            )
        )
    }
}