package com.autoresponder.app.ui.aiconfig

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.autoresponder.app.R
import com.autoresponder.app.databinding.FragmentAiConfigBinding
import com.google.android.material.textfield.TextInputEditText

class AIConfigFragment : Fragment() {

    private var _binding: FragmentAiConfigBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: android.content.SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        setupAIReplyToggle()
        setupAPIKey()
        setupLLMModel()
        setupLanguage()
        setupBotName()
        setupCustomPrompt()
    }

    private fun setupAIReplyToggle() {
        val isAiReplyEnabled = sharedPreferences.getBoolean("is_ai_reply_enabled", false)
        binding.aiReplyEnabledSwitch.isChecked = isAiReplyEnabled

        binding.aiReplyEnabledSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("is_ai_reply_enabled", isChecked).apply()
        }
    }

    private fun setupAPIKey() {
        val apiKey = sharedPreferences.getString("api_key", "") ?: ""
        binding.apiKeyEditText.setText(apiKey)

        binding.apiKeyEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                sharedPreferences.edit().putString("api_key", s?.toString() ?: "").apply()
            }
        })
    }

    private fun setupLLMModel() {
        val llmModels = resources.getStringArray(R.array.llm_models)
        val llmModelValues = resources.getStringArray(R.array.llm_model_values)
        
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, llmModels)
        binding.llmModelAutocomplete.setAdapter(adapter)

        val currentModel = sharedPreferences.getString("llm_model", "gpt-4o-mini") ?: "gpt-4o-mini"
        val currentIndex = llmModelValues.indexOf(currentModel)
        if (currentIndex >= 0) {
            binding.llmModelAutocomplete.setText(llmModels[currentIndex], false)
        }

        binding.llmModelAutocomplete.setOnItemClickListener { _, _, position, _ ->
            val selectedValue = llmModelValues[position]
            sharedPreferences.edit().putString("llm_model", selectedValue).apply()
        }
    }

    private fun setupLanguage() {
        val languages = resources.getStringArray(R.array.languages)
        
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, languages)
        binding.aiLanguageAutocomplete.setAdapter(adapter)

        val currentLanguage = sharedPreferences.getString("ai_reply_language", "English") ?: "English"
        binding.aiLanguageAutocomplete.setText(currentLanguage, false)

        binding.aiLanguageAutocomplete.setOnItemClickListener { _, _, position, _ ->
            val selectedLanguage = languages[position]
            sharedPreferences.edit().putString("ai_reply_language", selectedLanguage).apply()
        }
    }

    private fun setupBotName() {
        val botName = sharedPreferences.getString("bot_name", "Yuji") ?: "Yuji"
        binding.botNameEditText.setText(botName)

        binding.botNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                sharedPreferences.edit().putString("bot_name", s?.toString() ?: "Yuji").apply()
            }
        })
    }

    private fun setupCustomPrompt() {
        val customPrompt = sharedPreferences.getString("custom_ai_prompt", "") ?: ""
        binding.customPromptEditText.setText(customPrompt)

        binding.customPromptEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                sharedPreferences.edit().putString("custom_ai_prompt", s?.toString() ?: "").apply()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

