package ie.dylangore.mad1.assignment2.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * The first activity shown to the user
 */
class MainActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var binding: ActivityMainBinding

    /**
     * Runs when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure AppBar navigation
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_locations,
                R.id.nav_observation_stations,
                R.id.nav_warnings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Display a snack bar notification if the device is offline
        if (!NetworkHelper.isOnline(this.applicationContext)){
            Snackbar.make(navView, R.string.no_network, Snackbar.LENGTH_LONG).show()
        }
    }

    /**
     * Create the top menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * Handle actions for the top menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_settings -> {
                // Launch the settings activity
                val intent = Intent(this, SettingsActivity::class.java)
                startActivityForResult(intent, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Runs when an activity created within this activity is closed
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        info { "Re-creating MainActivity..." }
        // Re-create the current activity (to load in changes made in setting activity)
        this.recreate()
    }
}