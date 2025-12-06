package com.thebizarreabhishek.app.ui.automations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.thebizarreabhishek.app.databinding.FragmentAutomationsBinding;

public class AutomationsFragment extends Fragment {

    private FragmentAutomationsBinding binding;

    private android.content.SharedPreferences prefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAutomationsBinding.inflate(inflater, container, false);

        prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext());

        setupSwitches();
        setupFab();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext());

        // WhatsApp (Default True)
        setupToggle(prefs, binding.switchWhatsapp, "is_whatsapp_enabled", true);

        // Telegram
        setupToggle(prefs, binding.switchTelegram, "is_telegram_enabled", false);

        // Instagram
        setupToggle(prefs, binding.switchInstagram, "is_instagram_enabled", false);

        // Snapchat
        setupToggle(prefs, binding.switchSnapchat, "is_snapchat_enabled", false);

        // Twitter/X
        setupToggle(prefs, binding.switchTwitter, "is_twitter_enabled", false);
    }

    private void setupToggle(SharedPreferences prefs, com.google.android.material.switchmaterial.SwitchMaterial toggle,
            String key, boolean defaultValue) {
        toggle.setChecked(prefs.getBoolean(key, defaultValue));
        toggle.setOnCheckedChangeListener((buttonView, isChecked) -> prefs.edit().putBoolean(key, isChecked).apply());
    }

    private void setupSwitches() {
        // Busy Mode
        binding.switchBusy.setChecked(prefs.getBoolean("auto_busy_mode", false));
        binding.switchBusy.setOnCheckedChangeListener(
                (v, isChecked) -> prefs.edit().putBoolean("auto_busy_mode", isChecked).apply());

        // Meeting Reply
        binding.switchMeeting.setChecked(prefs.getBoolean("auto_meeting_reply", false));
        binding.switchMeeting.setOnCheckedChangeListener(
                (v, isChecked) -> prefs.edit().putBoolean("auto_meeting_reply", isChecked).apply());

        // Weekend Away
        binding.switchWeekend.setChecked(prefs.getBoolean("auto_weekend_away", false));
        binding.switchWeekend.setOnCheckedChangeListener(
                (v, isChecked) -> prefs.edit().putBoolean("auto_weekend_away", isChecked).apply());
    }

    private void setupFab() {
        binding.fabAddRule.setOnClickListener(v -> {
            // Placeholder for adding custom rules
            android.widget.Toast.makeText(requireContext(), "Add Custom Rule feature coming soon!",
                    android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
