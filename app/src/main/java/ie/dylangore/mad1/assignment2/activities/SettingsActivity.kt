package ie.dylangore.mad1.assignment2.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
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

        // Set initial state of switch
        binding.settingsSwitchWarningsTest.isChecked = app.useWarningTestAPI

        // Button: Load Example Data
        binding.settingsBtnLoadExampleData.setOnClickListener {
            // Add new location
            app.locations.add(Location(-1, "Waterford, Ireland", 52.2583300, -7.1119400, 13))
            app.locations.add(Location(-1, "New York, USA", 40.7142700, 74.0059700, 10))
            app.locations.add(Location(-1, "Longyearbyen, Svalbard", 78.2233400, 15.6468900, 1713))
            app.locations.add(Location(-1, "Sydney, Australia", -33.8678500, 151.2073200, 19))
            app.locations.add(Location(-1, "Tokyo, Japan", 35.6895000, 139.6917100, 40))

            toast(resources.getString(R.string.loaded_sample_data))
        }

        // Button: Delete all location data
        binding.settingsBtnDeleteFiles.setOnClickListener {
            // Create an alert dialog to ask the user to confirm deletion
            val alert = AlertDialog.Builder(this)
            alert.setTitle(R.string.title_location_delete_confirmation)

            // Yes button
            alert.setPositiveButton(R.string.yes) { _, _ ->
                // Clear existing data
                app.locations.empty()
                toast(resources.getString(R.string.deleted, ""))
            }

            // No button
            alert.setNegativeButton(R.string.no) { _, _ ->
                // Do nothing
            }

            // Display the alert
            alert.show()
        }

        // Warning test API Switch
        binding.settingsSwitchWarningsTest.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            app.useWarningTestAPI = b
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
                setResult(100)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}