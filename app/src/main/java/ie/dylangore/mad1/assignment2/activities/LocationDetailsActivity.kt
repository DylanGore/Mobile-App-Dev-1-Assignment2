package ie.dylangore.mad1.assignment2.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.ActivityLocationDetailsBinding
import ie.dylangore.mad1.assignment2.helpers.ForecastHelper
import ie.dylangore.mad1.assignment2.helpers.TimeHelper
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.Forecast
import ie.dylangore.mad1.assignment2.models.Location
import ie.dylangore.mad1.assignment2.services.ForecastRequestService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.*

/**
 * The activity used to view weather forecast details
 */
class LocationDetailsActivity : AppCompatActivity(), AnkoLogger, ForecastListener {

    private lateinit var app: MainApp
    private lateinit var binding: ActivityLocationDetailsBinding
    private lateinit var receiver : BroadcastReceiver
    private lateinit var location: Location
    private lateinit var forecast: Forecast

    /**
     * Runs when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityLocationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Show the refresh icon initially
        binding.layoutForecastRefresh.isRefreshing = true

        // Set the layout of the RecyclerView
        binding.recyclerViewForecast.layoutManager = LinearLayoutManager(this)

        // Define a broadcast receiver to receive data from the background service that handles getting data from the internet
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent?.hasExtra("forecast")!!){
                    forecast = intent.extras?.getParcelable<Forecast>("forecast")!!
                    info("Forecast Broadcast Received")
                    updateForecastDisplay()
                }else if (intent.hasExtra("forecast_none")){
                    toast(resources.getString(R.string.empty_text_forecast))
                }
                binding.layoutForecastRefresh.isRefreshing = false
            }
        }

        // Handle the pull to refresh action
        binding.layoutForecastRefresh.setOnRefreshListener {
            refreshForecast()
            toast(resources.getString(R.string.refreshing_forecast))
        }

        // Register the broadcast receiver
        val intentFilter = IntentFilter()
        intentFilter.addAction("ie.dylangore.weather")
        registerReceiver(receiver, intentFilter)

        // Edit button
        binding.forecastEditLocationButton.setOnClickListener {
            val intent = Intent(this, AddEditLocationActivity::class.java)
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
                toast(resources.getString(R.string.deleted, location.name))
                setResult(RESULT_OK)
                finish()
            }

            // No button
            alert.setNegativeButton(R.string.no) { _, _ ->
                // Do nothing
            }

            // Display the alert
            alert.show()
        }

        // Get location data passed from parent
        if (intent.hasExtra("location")) {
            location = intent.extras?.getParcelable("location")!!

            // If displaying a forecast for an observation station (id -1) hide the edit/delete location buttons
            if (location.id <= 0){
                binding.forecastDeleteLocationButton.isEnabled = false
                binding.forecastEditLocationButton.isEnabled = false
            }

            updateLocationDetails(location)
        }else{
            // If no data was passed to the activity, end it
            setResult(RESULT_CANCELED)
            finish()
        }

        checkIfEmpty()
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

    private fun updateForecastDisplay(){
        val recyclerView = binding.recyclerViewForecast

        // Setup the RecyclerView and Adapter
        recyclerView.adapter = ForecastAdapter(forecast.properties.timeseries as ArrayList<Forecast.TimeSeries>, this)
        recyclerView.adapter?.notifyDataSetChanged()

        checkIfEmpty()
    }

    private fun updateLocationDetails(newLocation: Location){
        // Update the existing location object with the new one
        location = newLocation
        // Set the title to the warning headline
        title = resources.getString(R.string.title_forecast, location.name)
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

    override fun onForecastClick(forecastData: Forecast.TimeSeries) {
        val intent = Intent(this, ForecastDetailsActivity::class.java)
        // Pass the selected forecast data through to the activity
        intent.putExtra("forecast_hourly", forecastData)
        // units
        intent.putExtra("forecast_units", forecast.properties.meta.units)
        // location
        intent.putExtra("forecast_location", location)
        startActivityForResult(intent, 0)
    }

    /**
     * Checks if the list of locations is empty. If it is,
     * display a view stating that.
     */
    private fun checkIfEmpty(){
        val recyclerView = binding.recyclerViewForecast
        val emptyLayout = binding.layoutEmptyForecast
        // Display a TextView if the list is empty
        if (this::forecast.isInitialized){
            emptyLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }else{
            emptyLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }
}

/**
 * Handle when a forecast list item is clicked on
 */
interface ForecastListener {
    fun onForecastClick(forecastData: Forecast.TimeSeries)
}

/**
 * Adapter to handle displaying a list of forecast entries
 */
private class ForecastAdapter(private var forecastEntries: ArrayList<Forecast.TimeSeries>, private val listener: ForecastListener) : RecyclerView.Adapter<ForecastAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.list_card,
                parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val forecast = forecastEntries[holder.adapterPosition]
        holder.bind(forecast, listener)
    }

    override fun getItemCount(): Int = forecastEntries.size

    private class MainHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(forecast: Forecast.TimeSeries, listener: ForecastListener){
            itemView.setOnClickListener { listener.onForecastClick(forecast) }
            // Get elements
            val title : TextView = itemView.findViewById(R.id.list_card_title)
            val description : TextView = itemView.findViewById(R.id.list_card_description)
            val icon : ImageView = itemView.findViewById(R.id.list_card_icon)
            val conditions = forecast.data.next1_Hours?.summary?.symbolCode
            // Set title
            title.text = TimeHelper.fromISO8601(forecast.time).toString()
            // Set Description
            description.text = ForecastHelper.getReadableCondition(conditions).toUpperCase(Locale.ROOT)
            description.visibility = View.VISIBLE
            // Set Icon
            icon.setImageDrawable(ContextCompat.getDrawable(itemView.context, ForecastHelper.getWeatherIcon(conditions)))
            icon.visibility = View.VISIBLE
        }
    }
}