package com.autoresponder.app.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autoresponder.app.databinding.ItemHistoryBinding
import com.autoresponder.app.helper.ContactHelper
import com.autoresponder.app.helper.PlatformDetector
import com.autoresponder.app.model.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private val messages: List<Message>,
    private val context: android.content.Context,
    private val onDeleteClick: (Message) -> Unit,
    private val onItemClick: (Message) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            // Get contact name or use phone number
            val contactName = if (ContactHelper.isPhoneNumber(message.sender)) {
                ContactHelper.getContactName(context, message.sender)
            } else {
                message.sender
            }

            // Display contact name
            binding.contactName.text = contactName

            // Show phone number only if it's different from contact name
            if (contactName != message.sender && ContactHelper.isPhoneNumber(message.sender)) {
                binding.phoneNumber.text = message.sender
                binding.phoneNumber.visibility = View.VISIBLE
            } else {
                binding.phoneNumber.visibility = View.GONE
            }

            // Display message and reply
            binding.messageText.text = message.message
            binding.replyText.text = message.reply

            // Display timestamp
            binding.timestamp.text = formatTimestamp(message.timestamp)

            // Display platform
            binding.platformText.text = message.platform.replaceFirstChar { 
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
            }

            // Set platform icon
            val platformIcon = when (message.platform.lowercase()) {
                "whatsapp" -> com.autoresponder.app.R.drawable.ic_whatsapp
                "instagram" -> com.autoresponder.app.R.drawable.ic_instagram
                "facebook" -> com.autoresponder.app.R.drawable.ic_facebook
                "telegram" -> com.autoresponder.app.R.drawable.ic_telegram
                "twitter" -> com.autoresponder.app.R.drawable.ic_twitter
                else -> com.autoresponder.app.R.drawable.ic_service_status
            }
            binding.platformIcon.setImageResource(platformIcon)

            // Set click listeners
            binding.moreOptionsButton.setOnClickListener {
                onDeleteClick(message)
            }

            binding.root.setOnClickListener {
                onItemClick(message)
            }
        }

        private fun formatTimestamp(timestamp: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                val date = inputFormat.parse(timestamp)
                if (date != null) {
                    outputFormat.format(date)
                } else {
                    timestamp
                }
            } catch (e: Exception) {
                timestamp
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size
}

