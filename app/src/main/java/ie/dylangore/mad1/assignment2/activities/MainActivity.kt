package ie.dylangore.mad1.assignment2.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.ActivityMainBinding
import ie.dylangore.mad1.assignment2.helpers.NetworkHelper
import ie.dylangore.mad1.assignment2.models.Warning
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * The first activity shown to the user
 */
class MainActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure AppBar navigation
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_locations,
            R.id.nav_observation_stations,
            R.id.nav_warnings
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Display a snack bar notification if the device is offline
        if (!NetworkHelper.isOnline(this.applicationContext)){
            Snackbar.make(navView, "No network connection!", Snackbar.LENGTH_LONG).show()
        }
    }
}