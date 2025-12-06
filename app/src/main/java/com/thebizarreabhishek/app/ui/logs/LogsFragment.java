package com.thebizarreabhishek.app.ui.logs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.thebizarreabhishek.app.databinding.FragmentLogsBinding;

public class LogsFragment extends Fragment {

    private FragmentLogsBinding binding;

    private LogsAdapter adapter;
    private com.thebizarreabhishek.app.helpers.DatabaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLogsBinding.inflate(inflater, container, false);

        setupRecyclerView();
        setupListeners();

        dbHelper = new com.thebizarreabhishek.app.helpers.DatabaseHelper(requireContext());
        loadLogs();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new LogsAdapter();
        binding.recyclerLogs.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.btnFilter.setOnClickListener(v -> {
            // Placeholder for filter dialog
            android.widget.Toast
                    .makeText(requireContext(), "Filtering not implemented yet", android.widget.Toast.LENGTH_SHORT)
                    .show();
        });

        binding.btnClear.setOnClickListener(v -> {
            dbHelper.deleteOldMessages(); // Or a custom method to delete ALL
            loadLogs();
            android.widget.Toast.makeText(requireContext(), "Logs Cleared", android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    private void loadLogs() {
        new Thread(() -> {
            java.util.List<com.thebizarreabhishek.app.models.Message> messages = dbHelper.getAllMessages();
            requireActivity().runOnUiThread(() -> {
                if (messages.isEmpty()) {
                    // Show empty state if we had one, for now just empty list
                    android.widget.Toast.makeText(requireContext(), "No logs found", android.widget.Toast.LENGTH_SHORT)
                            .show();
                }
                adapter.setMessages(messages);
            });
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLogs();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
