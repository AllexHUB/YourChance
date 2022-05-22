package com.grigorenko.yourchance.ui.startuper.messages.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grigorenko.yourchance.R
import com.grigorenko.yourchance.domain.model.ChatPresentation
import com.grigorenko.yourchance.databinding.ChatItemBinding
import com.grigorenko.yourchance.ui.startuper.messages.ChatClickListener
import com.squareup.picasso.Picasso

class UserChatsAdapter(
    private val chatClickListener: ChatClickListener
) : ListAdapter<ChatPresentation, UserChatViewHolder>(ChatDiffUtil) {
    companion object {
        private object ChatDiffUtil : DiffUtil.ItemCallback<ChatPresentation>() {
            override fun areItemsTheSame(
                oldItem: ChatPresentation,
                newItem: ChatPresentation
            ): Boolean {
                return oldItem.companion.icon == newItem.companion.icon
            }

            override fun areContentsTheSame(
                oldItem: ChatPresentation,
                newItem: ChatPresentation
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserChatViewHolder {
        return UserChatViewHolder(
            ChatItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), chatClickListener
        )
    }

    override fun onBindViewHolder(holder: UserChatViewHolder, position: Int) {
        val chatPresentation = getItem(position)
        holder.bindUserChat(chatPresentation)
    }

    override fun submitList(list: List<ChatPresentation>?) {
        super.submitList(list?.let {
            ArrayList(it)
        })
    }
}

class UserChatViewHolder(
    private val chatItemBinding: ChatItemBinding,
    private val chatClickListener: ChatClickListener
) : RecyclerView.ViewHolder(chatItemBinding.root) {

    fun bindUserChat(chatPresentation: ChatPresentation) {
        chatItemBinding.apply {
            Picasso.get()
                .load(chatPresentation.companion.icon.uri.toUri())
                .fit().centerCrop()
                .placeholder(R.drawable.ic_loading_image)
                .into(companionIcon)
            textName.text = chatPresentation.companion.fullName
            lastMessage.text = chatPresentation.lastMessage.text
            chatItem.setOnClickListener {
                chatClickListener.onClick(
                    chatPresentation.firstUserUID,
                    chatPresentation.secondUserUID, chatPresentation.companion
                )
            }
        }
    }
}

