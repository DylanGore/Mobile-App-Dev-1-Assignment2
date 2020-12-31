package ie.dylangore.mad1.assignment2.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.helpers.NetworkHelper
import org.jetbrains.anko.AnkoLogger

/**
 * The first activity shown to the user
 */
class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configure AppBar navigation
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_locations,
            R.id.nav_observation_stations
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Display a snack bar notification if the device is offline
        if (!NetworkHelper.isOnline(this.applicationContext)){
            Snackbar.make(navView, "No network connection!", Snackbar.LENGTH_LONG).show()
        }
    }

}