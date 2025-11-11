package com.autoresponder.app.ui.presetreplies

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.autoresponder.app.R
import com.autoresponder.app.database.DatabaseHelper
import com.autoresponder.app.databinding.FragmentPresetRepliesBinding
import com.autoresponder.app.databinding.ItemPresetBinding
import com.autoresponder.app.model.Preset
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PresetRepliesFragment : Fragment() {

    private var _binding: FragmentPresetRepliesBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var presetAdapter: PresetAdapter
    private val presets = mutableListOf<Preset>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPresetRepliesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())
        setupRecyclerView()
        loadPresets()

        binding.addPresetButton.setOnClickListener {
            showAddPresetDialog()
        }

        binding.fabAddPreset.setOnClickListener {
            showAddPresetDialog()
        }
    }

    private fun setupRecyclerView() {
        presetAdapter = PresetAdapter(presets) { preset ->
            showEditPresetDialog(preset)
        } { preset ->
            showDeleteConfirmDialog(preset)
        }

        binding.presetsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.presetsRecyclerView.adapter = presetAdapter
    }

    private fun loadPresets() {
        presets.clear()
        presets.addAll(dbHelper.getAllPresets())
        presetAdapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (presets.isEmpty()) {
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.presetsRecyclerView.visibility = View.GONE
        } else {
            binding.emptyStateLayout.visibility = View.GONE
            binding.presetsRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun showAddPresetDialog() {
        showPresetDialog(null)
    }

    private fun showEditPresetDialog(preset: Preset) {
        showPresetDialog(preset)
    }

    private fun showPresetDialog(preset: Preset?) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_preset, null)

        val dialogTitleText = dialogView.findViewById<android.widget.TextView>(R.id.dialog_title)
        val keywordEditText = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.keyword_edit_text)
        val replyEditText = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.reply_edit_text)
        val caseSensitiveSwitch = dialogView.findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.case_sensitive_switch)
        val exactMatchSwitch = dialogView.findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.exact_match_switch)

        dialogTitleText.text = if (preset == null) "Add Preset Reply" else "Edit Preset Reply"

        if (preset != null) {
            keywordEditText.setText(preset.keyword)
            replyEditText.setText(preset.reply)
            caseSensitiveSwitch.isChecked = preset.isCaseSensitive
            exactMatchSwitch.isChecked = preset.isExactMatch
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save), null)
            .setNegativeButton(getString(R.string.cancel), null)
            .create()

        dialog.setOnShowListener {
            val saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            saveButton.setOnClickListener {
                val keyword = keywordEditText.text?.toString()?.trim() ?: ""
                val reply = replyEditText.text?.toString()?.trim() ?: ""

                if (keyword.isEmpty() || reply.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (preset == null) {
                    dbHelper.insertPreset(
                        keyword,
                        reply,
                        caseSensitiveSwitch.isChecked,
                        exactMatchSwitch.isChecked
                    )
                    Toast.makeText(requireContext(), "Preset added", Toast.LENGTH_SHORT).show()
                } else {
                    dbHelper.updatePreset(
                        preset.id,
                        keyword,
                        reply,
                        caseSensitiveSwitch.isChecked,
                        exactMatchSwitch.isChecked
                    )
                    Toast.makeText(requireContext(), "Preset updated", Toast.LENGTH_SHORT).show()
                }

                dialog.dismiss()
                loadPresets()
            }
        }

        dialog.show()
    }

    private fun showDeleteConfirmDialog(preset: Preset) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_preset))
            .setMessage("Are you sure you want to delete this preset?")
            .setPositiveButton("Delete") { _, _ ->
                dbHelper.deletePreset(preset.id)
                Toast.makeText(requireContext(), "Preset deleted", Toast.LENGTH_SHORT).show()
                loadPresets()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class PresetAdapter(
        private val presetList: List<Preset>,
        private val onEditClick: (Preset) -> Unit,
        private val onDeleteClick: (Preset) -> Unit
    ) : androidx.recyclerview.widget.RecyclerView.Adapter<PresetAdapter.PresetViewHolder>() {

        inner class PresetViewHolder(val binding: ItemPresetBinding) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresetViewHolder {
            val binding = ItemPresetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return PresetViewHolder(binding)
        }

        override fun onBindViewHolder(holder: PresetViewHolder, position: Int) {
            val preset = presetList[position]
            holder.binding.presetKeywordText.text = preset.keyword
            holder.binding.presetReplyText.text = preset.reply

            if (preset.isCaseSensitive) {
                holder.binding.caseSensitiveChip.visibility = View.VISIBLE
            } else {
                holder.binding.caseSensitiveChip.visibility = View.GONE
            }

            if (preset.isExactMatch) {
                holder.binding.exactMatchChip.visibility = View.VISIBLE
            } else {
                holder.binding.exactMatchChip.visibility = View.GONE
            }

            holder.binding.editPresetButton.setOnClickListener {
                onEditClick(preset)
            }

            holder.binding.deletePresetButton.setOnClickListener {
                onDeleteClick(preset)
            }
        }

        override fun getItemCount(): Int = presetList.size
    }
}

