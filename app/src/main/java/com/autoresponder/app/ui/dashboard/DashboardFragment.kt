package com.autoresponder.app.ui.dashboard

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.autoresponder.app.R
import com.autoresponder.app.database.DatabaseHelper
import com.autoresponder.app.databinding.FragmentDashboardBinding
import com.autoresponder.app.helper.NotificationHelper
import com.autoresponder.app.service.AutoReplyNotificationService

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        dbHelper = DatabaseHelper(requireContext())

        setupSwitches()
        updateStats()
        updateServiceStatus()
    }

    private fun setupSwitches() {
        // Autoreply switch
        val isBotEnabled = sharedPreferences.getBoolean("is_bot_enabled", true)
        binding.autoreplySwitch.isChecked = isBotEnabled
        updateAutoreplySummary(isBotEnabled)
        updateAutoreplyOptionsVisibility(isBotEnabled)

        binding.autoreplySwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("is_bot_enabled", isChecked).apply()
            updateAutoreplySummary(isChecked)
            updateAutoreplyOptionsVisibility(isChecked)
            updateServiceStatus()
            
            // Start/stop service
            if (NotificationHelper.isNotificationServicePermissionGranted(requireContext())) {
                val intent = android.content.Intent(requireContext(), AutoReplyNotificationService::class.java)
                if (isChecked) {
                    requireContext().startService(intent)
                } else {
                    requireContext().stopService(intent)
                }
            }
        }

        // AI Reply switch
        val isAiReplyEnabled = sharedPreferences.getBoolean("is_ai_reply_enabled", false)
        binding.aiReplySwitch.isChecked = isAiReplyEnabled
        updateAiReplySummary(isAiReplyEnabled)

        binding.aiReplySwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("is_ai_reply_enabled", isChecked).apply()
            updateAiReplySummary(isChecked)
        }

        // Preset Replies button
        binding.presetRepliesButton.setOnClickListener {
            // Navigate to preset replies fragment
            findNavController().navigate(R.id.nav_preset_replies)
        }
    }

    private fun updateAutoreplyOptionsVisibility(enabled: Boolean) {
        binding.autoreplyOptionsContainer.visibility = if (enabled) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun updateAutoreplySummary(enabled: Boolean) {
        binding.autoreplySummary.text = if (enabled) {
            getString(R.string.autoreply_enabled)
        } else {
            getString(R.string.autoreply_disabled)
        }
    }

    private fun updateAiReplySummary(enabled: Boolean) {
        binding.aiReplySummary.text = if (enabled) {
            getString(R.string.ai_reply_enabled)
        } else {
            getString(R.string.ai_reply_disabled)
        }
    }

    private fun updateStats() {
        val totalReplies = dbHelper.getTotalRepliesCount()
        val todayReplies = dbHelper.getTodayRepliesCount()
        
        binding.totalRepliesText.text = totalReplies.toString()
        binding.todayRepliesText.text = todayReplies.toString()
    }

    private fun updateServiceStatus() {
        val hasPermission = NotificationHelper.isNotificationServicePermissionGranted(requireContext())
        val isEnabled = sharedPreferences.getBoolean("is_bot_enabled", true)
        val isRunning = NotificationHelper.isNotificationListenerServiceRunning(requireContext())

        val status = when {
            !hasPermission -> "Permission not granted"
            !isEnabled -> "Service disabled"
            isRunning && isEnabled -> "Service running"
            else -> "Service stopped"
        }

        binding.serviceStatusText.text = status
    }

    override fun onResume() {
        super.onResume()
        updateStats()
        updateServiceStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

