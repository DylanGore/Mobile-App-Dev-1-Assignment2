package ie.dylangore.mad1.assignment2.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.davidmoten.geo.GeoHash
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.ActivityObservationStationBinding
import ie.dylangore.mad1.assignment2.models.ObservationStation
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * The activity used to view details about a specific observation station
 */
class ObservationStationActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var binding:ActivityObservationStationBinding

    /**
     * Runs when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObservationStationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("station_view")) {
            val station : ObservationStation.ObservationStationItem = intent.extras?.getParcelable("station_view")!!

            // Set the title to the station name
            title = station.location

            // Display weather data
            binding.stationWeatherDescription.text = resources.getString(R.string.station_weather_description, station.weatherDescription)
            binding.stationTemperature.text = resources.getString(R.string.station_temperature, station.temperature.toString())
            binding.stationHumidity.text = resources.getString(R.string.station_humidity, station.humidity.toString())
            binding.stationPressure.text = resources.getString(R.string.station_pressure, station.pressure.toString())
            binding.stationWind.text = String.format(resources.getString(R.string.station_wind), station.wind_speed.toString(), station.wind_direction_str)
            binding.stationWindGust.text = resources.getString(R.string.station_wind_gust, station.wind_gust.toString())
            binding.stationLastUpdated.text = resources.getString(R.string.station_last_updated, station.time)

            // Decode geohash
            val coordinates = GeoHash.decodeHash(station.geohash)
            info { "Lat: ${coordinates.lat} Lon: ${coordinates.lon}" }

            // Display location data
            binding.stationGeohash.text = resources.getString(R.string.station_geohash, station.geohash)
            binding.stationLatitude.text = resources.getString(R.string.station_latitude, coordinates.lat.toString())
            binding.stationLongitude.text = resources.getString(R.string.station_longitude, coordinates.lon.toString())
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
}