package com.autoresponder.app.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.autoresponder.app.R
import com.autoresponder.app.databinding.ActivityMainBinding
import com.autoresponder.app.helper.NotificationHelper
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment)
        
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_dashboard, R.id.nav_platforms, R.id.nav_ai_config, R.id.nav_history, R.id.nav_settings, R.id.nav_about),
            binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener(this)

        binding.bottomNavigation.setupWithNavController(navController)

        // Check notification permission
        if (!NotificationHelper.isNotificationServicePermissionGranted(this)) {
            showPermissionSnackbar()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_dashboard -> {
                navController.navigate(R.id.nav_dashboard)
            }
            R.id.nav_platforms -> {
                navController.navigate(R.id.nav_platforms)
            }
            R.id.nav_ai_config -> {
                navController.navigate(R.id.nav_ai_config)
            }
            R.id.nav_history -> {
                navController.navigate(R.id.nav_history)
            }
            R.id.nav_settings -> {
                navController.navigate(R.id.nav_settings)
            }
            R.id.nav_about -> {
                navController.navigate(R.id.nav_about)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showPermissionSnackbar() {
        Snackbar.make(
            binding.root,
            getString(R.string.notification_permission_message),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.grant_permission) {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }.show()
    }


    override fun onResume() {
        super.onResume()
        // Update UI based on permission status
        if (NotificationHelper.isNotificationServicePermissionGranted(this)) {
            // Permission granted, service should be running
        }
    }
}

