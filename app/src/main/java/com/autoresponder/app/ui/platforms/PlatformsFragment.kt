package com.autoresponder.app.ui.platforms

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.autoresponder.app.databinding.FragmentPlatformsBinding

class PlatformsFragment : Fragment() {

    private var _binding: FragmentPlatformsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlatformsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        setupPlatformSwitches()
    }

    private fun setupPlatformSwitches() {
        // WhatsApp
        binding.whatsappSwitch.isChecked = sharedPreferences.getBoolean("platform_whatsapp_enabled", true)
        binding.whatsappSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("platform_whatsapp_enabled", isChecked).apply()
        }

        // Instagram
        binding.instagramSwitch.isChecked = sharedPreferences.getBoolean("platform_instagram_enabled", true)
        binding.instagramSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("platform_instagram_enabled", isChecked).apply()
        }

        // Facebook
        binding.facebookSwitch.isChecked = sharedPreferences.getBoolean("platform_facebook_enabled", true)
        binding.facebookSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("platform_facebook_enabled", isChecked).apply()
        }

        // Telegram
        binding.telegramSwitch.isChecked = sharedPreferences.getBoolean("platform_telegram_enabled", true)
        binding.telegramSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("platform_telegram_enabled", isChecked).apply()
        }

        // Twitter/X
        binding.twitterSwitch.isChecked = sharedPreferences.getBoolean("platform_twitter_enabled", true)
        binding.twitterSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("platform_twitter_enabled", isChecked).apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

