package com.grigorenko.yourchance.ui.startuper.messages.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.grigorenko.yourchance.databinding.FragmentChatBinding
import com.grigorenko.yourchance.domain.model.Message
import com.grigorenko.yourchance.domain.viewmodel.ChatViewModel
import com.grigorenko.yourchance.ui.MainActivity
import com.grigorenko.yourchance.ui.startuper.messages.adapters.ChatAdapter
import java.util.*

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val args: ChatFragmentArgs by navArgs()
    private lateinit var adapter: ChatAdapter

    private val chatViewModel: ChatViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        (activity as MainActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ChatAdapter(args.firstUserId, args.companion.icon)
        val userIds = listOf(args.firstUserId, args.secondUserId)
        chatViewModel.apply {
            manageChatMessages(userIds)
            messages.observe(viewLifecycleOwner) {
                adapter.submitList(it)
                if (!it.isNullOrEmpty()) {
                    binding.apply {
                        progressBar.visibility = View.GONE
                        messagesRecyclerView.visibility = View.VISIBLE
                        messagesRecyclerView.smoothScrollToPosition(it.size - 1)
                    }
                }
            }
        }
        binding.apply {
            messagesRecyclerView.adapter = adapter
            messagesRecyclerView.layoutManager = LinearLayoutManager(context)
            textName.text = args.companion.fullName
            navBack.setOnClickListener {
                findNavController().navigateUp()
            }
            sendMessage.setOnClickListener {
                val message = Message(
                    System.currentTimeMillis().toString(),
                    args.firstUserId,
                    inputMessage.text.toString(),
                    Date()
                )
                chatViewModel.addMessageToChat(userIds, message)
                inputMessage.text.clear()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).supportActionBar?.show()
        chatViewModel.messages.value = null
        binding.messagesRecyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}