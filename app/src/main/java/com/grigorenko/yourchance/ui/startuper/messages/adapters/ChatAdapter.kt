package com.grigorenko.yourchance.ui.startuper.messages.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grigorenko.yourchance.database.model.Image
import com.grigorenko.yourchance.database.model.Message
import com.grigorenko.yourchance.databinding.ReceivedMessageItemBinding
import com.grigorenko.yourchance.databinding.SentMessageItemBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(
    private val userId: String,
    private val companionIcon: Image
) : ListAdapter<Message, RecyclerView.ViewHolder>(ChatDifCallback) {

    companion object {
        private object ChatDifCallback: DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Message,
                newItem: Message
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private val typeSent = 1
    private val typeReceived = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == typeSent)
            return SentMessageViewHolder(
                SentMessageItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        else
            return ReceivedMessageViewHolder(
                ReceivedMessageItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        if (getItemViewType(position) == typeSent) {
            (holder as SentMessageViewHolder).bindSentMessage(message)
        }
        else {
            (holder as ReceivedMessageViewHolder).bindReceivedMessage(
                message,
                companionIcon
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return if (message.senderId == userId)
            typeSent
        else
            typeReceived
    }

    override fun submitList(list: List<Message>?) {
        super.submitList(list?.let {
            ArrayList(it)
        })
    }
}

class SentMessageViewHolder(
    private val sentMessageItemBinding: SentMessageItemBinding
) : RecyclerView.ViewHolder(sentMessageItemBinding.root) {

    fun bindSentMessage(message: Message) {
        sentMessageItemBinding.apply {
            textMessage.text = message.text
            textDateTime.text = getReadableDate(message.date)
        }
    }
}

class ReceivedMessageViewHolder(
    private val receivedMessageItemBinding: ReceivedMessageItemBinding
) : RecyclerView.ViewHolder(receivedMessageItemBinding.root) {

    fun bindReceivedMessage(message: Message, image: Image) {
        receivedMessageItemBinding.apply {
            textMessage.text = message.text
            textDateTime.text = getReadableDate(message.date)
            Picasso.get()
                .load(image.uri.toUri())
                .fit().centerCrop()
                .into(userIcon)
        }
    }
}

fun getReadableDate(date: Date): String {
    return SimpleDateFormat("MMMM dd, hh:mm, a", Locale.getDefault()).format(date)
}