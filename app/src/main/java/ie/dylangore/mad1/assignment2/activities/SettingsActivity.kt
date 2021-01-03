package ie.dylangore.mad1.assignment2.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.ActivitySettingsBinding
import ie.dylangore.mad1.assignment2.helpers.FileHelper
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.Location
import org.jetbrains.anko.toast

/**
 * The activity used to allow the user to manage locally stored data and general settings
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var app: MainApp
    private lateinit var binding: ActivitySettingsBinding

    /**
     * Runs when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set title
        title = resources.getString(R.string.title_settings)

        // Button: Load Example Data
        binding.settingsBtnLoadExampleData.setOnClickListener {
            // Clear existing data
            app.locations.empty()
            // Add new location
            app.locations.add(Location(-1, "WIT", 52.2461, 7.1387, 0))

            toast("Loaded sample data")
        }

        // Button: Delete local files TODO: Fix
        binding.settingsBtnDeleteFiles.isEnabled = false
        binding.settingsBtnDeleteFiles.setOnClickListener {
            FileHelper.delete(this, "locations.json")
        }

    }

    /**
     * Create the top menu buttons
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_back, menu)
        return true
    }

    /**
     * Handle actions for the top menu buttons
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_option_back -> {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}