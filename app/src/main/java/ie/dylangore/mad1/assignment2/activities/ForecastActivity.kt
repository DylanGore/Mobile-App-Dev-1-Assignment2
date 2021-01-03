package ie.dylangore.mad1.assignment2.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.ActivityForecastBinding
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.Forecast
import ie.dylangore.mad1.assignment2.models.Location
import ie.dylangore.mad1.assignment2.services.ForecastRequestService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * The activity used to view weather forecast details
 */
class ForecastActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var app: MainApp
    private lateinit var binding: ActivityForecastBinding
    private lateinit var receiver : BroadcastReceiver
    private lateinit var location: Location
    private lateinit var forecast: Forecast

    /**
     * Runs when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Define a broadcast receiver to receive data from the background service that handles getting data from the internet
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent?.hasExtra("forecast")!!){
                    forecast = intent.extras?.getParcelable<Forecast>("forecast")!!
                    info("Forecast Broadcast Received")
//                    binding.layoutWarningsRefresh.isRefreshing = false

                    binding.forecastText.text = forecast.toString()
                }else if (intent?.hasExtra("forecast_none")){
                    binding.forecastText.text = "No data returned!"
                }
            }
        }

        // Register the broadcast receiver
        val intentFilter = IntentFilter()
        intentFilter.addAction("ie.dylangore.weather")
        registerReceiver(receiver, intentFilter)

        // Edit button
        binding.forecastEditLocationButton.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            // Pass the selected location data through to the activity
            intent.putExtra("location_edit", location)
            startActivityForResult(intent, 0)
        }

        // Delete Button
        binding.forecastDeleteLocationButton.setOnClickListener {
            // Create an alert dialog to ask the user to confirm deletion
            val alert = AlertDialog.Builder(this)
            alert.setTitle(R.string.title_location_delete_confirmation)

            // Yes button
            alert.setPositiveButton(R.string.yes) { _, _ ->
                app.locations.delete(location.id) // Delete the location from the store
                Toast.makeText(this, "Deleted ${location.name}", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }

            // No button
            alert.setNegativeButton(R.string.no) { _, _ ->
                setResult(RESULT_CANCELED)
                finish()
            }

            // Display the alert
            alert.show()
        }

        if (intent.hasExtra("location")) {
            location = intent.extras?.getParcelable("location")!!

            updateLocationDetails(location)
        }else{
            // If no data was passed to the activity, end it
            setResult(RESULT_CANCELED)
            finish()
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

    /**
     * Call a background service to send a GET request to update
     * the list of warnings.
     */
    private fun refreshForecast(){
        val intent = Intent(this,ForecastRequestService::class.java)
        intent.putExtra("location", location)
        ForecastRequestService.enqueueWork(this, intent)
    }

    private fun updateLocationDetails(newLocation: Location){
        // Update the existing location object with the new one
        location = newLocation
        // Set the title to the warning headline
        title = location.name
        // Get the forecast
        refreshForecast()
    }

    /**
     * Runs when an activity created within this fragment is closed
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Updated the displayed list
        if(app.locations.findOne(location.id) != null){
            updateLocationDetails(app.locations.findOne(location.id)!!)
        }
    }
}