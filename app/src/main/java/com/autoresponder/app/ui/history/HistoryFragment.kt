package com.autoresponder.app.ui.history

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.autoresponder.app.R
import com.autoresponder.app.database.DatabaseHelper
import com.autoresponder.app.databinding.FragmentHistoryBinding
import com.autoresponder.app.model.Message
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: HistoryAdapter
    private val messages = mutableListOf<Message>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // Setup RecyclerView
        adapter = HistoryAdapter(
            messages,
            requireContext(),
            onDeleteClick = { message -> showDeleteDialog(message) },
            onItemClick = { message -> showMessageDetails(message) }
        )
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = adapter

        // Request contacts permission if not granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS
            )
        }

        // Load messages
        loadMessages()

        // Manage history button
        binding.manageHistoryButton.setOnClickListener {
            showManageHistoryDialog()
        }
    }

    private fun loadMessages() {
        messages.clear()
        messages.addAll(dbHelper.getAllMessages())
        adapter.notifyDataSetChanged()

        // Show/hide empty state
        if (messages.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.historyRecyclerView.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.historyRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun showDeleteDialog(message: Message) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_confirm)
            .setMessage(R.string.delete_confirm_message)
            .setPositiveButton(R.string.ok) { _, _ ->
                dbHelper.deleteMessage(message.id)
                loadMessages()
                Toast.makeText(requireContext(), R.string.message_deleted, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showMessageDetails(message: Message) {
        // Show message details in a dialog
        val dialogView = layoutInflater.inflate(R.layout.dialog_message_details, null)
        // TODO: Implement message details dialog if needed
        // For now, just show a toast
        Toast.makeText(
            requireContext(),
            "Message: ${message.message}\nReply: ${message.reply}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showManageHistoryDialog() {
        val options = arrayOf(
            getString(R.string.clear_history_now),
            getString(R.string.delete_all_messages)
        )

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.manage_history)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showClearHistoryConfirmDialog()
                    1 -> showDeleteAllConfirmDialog()
                }
            }
            .show()
    }

    private fun showClearHistoryConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.clear_history_now)
            .setMessage(R.string.delete_all_confirm_message)
            .setPositiveButton(R.string.ok) { _, _ ->
                dbHelper.deleteAllMessages()
                loadMessages()
                Toast.makeText(requireContext(), R.string.history_cleared, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showDeleteAllConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_all_messages)
            .setMessage(R.string.delete_all_confirm_message)
            .setPositiveButton(R.string.ok) { _, _ ->
                dbHelper.deleteAllMessages()
                loadMessages()
                Toast.makeText(requireContext(), R.string.messages_deleted, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, reload messages to show contact names
                loadMessages()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload messages when fragment resumes
        loadMessages()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_READ_CONTACTS = 100
    }
}
