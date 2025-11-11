package com.autoresponder.app.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.autoresponder.app.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // GitHub button
        binding.githubButton.setOnClickListener {
            openUrl("https://github.com/thebizzareabhishek")
        }

        // PayPal button
        binding.paypalButton.setOnClickListener {
            openUrl("https://www.paypal.me/TheGreatBabaAbhishek")
        }

        // Buy me a coffee button (also opens PayPal)
        binding.coffeeButton.setOnClickListener {
            openUrl("https://www.paypal.me/TheGreatBabaAbhishek")
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            // Handle error if no browser is available
            android.widget.Toast.makeText(
                requireContext(),
                "Unable to open link",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

