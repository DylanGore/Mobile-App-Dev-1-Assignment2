package ie.dylangore.mad1.assignment2.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.ActivityForecastDetailsBinding
import ie.dylangore.mad1.assignment2.helpers.ForecastHelper
import ie.dylangore.mad1.assignment2.helpers.TimeHelper
import ie.dylangore.mad1.assignment2.models.Forecast
import ie.dylangore.mad1.assignment2.models.Location
import org.jetbrains.anko.AnkoLogger
import java.util.*

/**
 * The activity used to view forecast details for a specific forecast entry
 */
class ForecastDetailsActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var binding: ActivityForecastDetailsBinding
    private lateinit var forecast: Forecast.TimeSeries
    private lateinit var units: Forecast.Units
    private lateinit var location: Location

    /**
     * Runs when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get forecast data from parent activity
        if (intent.hasExtra("forecast_hourly")) {
            forecast = intent.extras?.getParcelable("forecast_hourly")!!
        }

        // Get forecast units from parent activity
        if (intent.hasExtra("forecast_units")) {
            units = intent.extras?.getParcelable("forecast_units")!!
        }

        // Get location from parent activity
        if (intent.hasExtra("forecast_location")) {
            location = intent.extras?.getParcelable("forecast_location")!!
        }

        updateForecastDetailsDisplay()
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
     * Update all of the display elements with the correct information
     *
     */
    private fun updateForecastDetailsDisplay(){
        
        // Extract data from forecast
        val conditions = forecast.data.next1_Hours?.summary?.symbolCode
        val pressure = "${forecast.data.instant.details.airPressureAtSeaLevel} ${units.airPressureAtSeaLevel}"
        val temperature = "${forecast.data.instant.details.airTemperature} ${units.airTemperature}"
        val humidity = "${forecast.data.instant.details.relativeHumidity} ${units.relativeHumidity}"
        val windDir = "${forecast.data.instant.details.windFromDirection} ${units.windFromDirection}"
        val windSpeed = "${forecast.data.instant.details.windSpeed} ${units.windSpeed}"
        val cloud = "${forecast.data.instant.details.cloudAreaFraction} ${units.cloudAreaFraction}"

        // Set activity title
        title = location.name + ": " + TimeHelper.fromISO8601(forecast.time)

        // Set icon
        binding.forecastDetailsIcon.setImageDrawable(ContextCompat.getDrawable(this, ForecastHelper.getWeatherIcon(conditions)))

        // Set details
        binding.forecastDetailsConditions.text = ForecastHelper.getReadableCondition(conditions).toUpperCase(Locale.ROOT)
        binding.forecastDetailsTemperature.text = resources.getString(R.string.forecast_details_temperature, temperature)
        binding.forecastDetailsPressure.text = resources.getString(R.string.forecast_details_pressure, pressure)
        binding.forecastDetailsHumidity.text = resources.getString(R.string.forecast_details_humidity, humidity)
        binding.forecastDetailsWindDir.text = resources.getString(R.string.forecast_details_wind_dir, windDir)
        binding.forecastDetailsWindSpeed.text = resources.getString(R.string.forecast_details_wind_speed, windSpeed)
        binding.forecastDetailsCloud.text = resources.getString(R.string.forecast_details_cloud, cloud)
    }
}