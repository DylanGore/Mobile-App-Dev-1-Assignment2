package ie.dylangore.mad1.assignment2.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.davidmoten.geo.GeoHash
import com.github.davidmoten.geo.LatLong
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import ie.dylangore.mad1.assignment2.BuildConfig
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.ActivityObservationStationBinding
import ie.dylangore.mad1.assignment2.helpers.TimeHelper
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.Location
import ie.dylangore.mad1.assignment2.models.ObservationStation
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast


/**
 * The activity used to view details about a specific observation station
 */
class ObservationStationActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var app:MainApp
    private lateinit var binding: ActivityObservationStationBinding
    private lateinit var coordinates: LatLong
    private lateinit var station: ObservationStation.ObservationStationItem
    private lateinit var stationAsLocation: Location
    private lateinit var mapboxMap: MapboxMap

    /**
     * Runs when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, BuildConfig.MapboxAccessToken) // this must be done before the layout is inflated
        binding = ActivityObservationStationBinding.inflate(layoutInflater)
        app = application as MainApp
        setContentView(binding.root)

        // Get the station details passed by the parent activity
        if (intent.hasExtra("station_view")) {
            station = intent.extras?.getParcelable("station_view")!!

            // Set the title to the station name
            title = station.location

            // Display weather data
            binding.stationWeatherDescription.text = resources.getString(
                R.string.station_weather_description,
                station.weatherDescription
            )
            binding.stationTemperature.text = resources.getString(
                R.string.station_temperature,
                station.temperature.toString()
            )
            binding.stationHumidity.text = resources.getString(
                R.string.station_humidity,
                station.humidity.toString()
            )
            binding.stationPressure.text = resources.getString(
                R.string.station_pressure,
                station.pressure.toString()
            )
            binding.stationWind.text = getString(R.string.station_wind, station.wind_speed.toString(), station.wind_direction_str)
            binding.stationWindGust.text = resources.getString(
                R.string.station_wind_gust,
                station.wind_gust.toString()
            )
            binding.stationLastUpdated.text = resources.getString(
                R.string.station_last_updated,
                TimeHelper.fromISO8601(station.time)
            )

            // Decode geohash
            coordinates = GeoHash.decodeHash(station.geohash)
            info { "Lat: ${coordinates.lat} Lon: ${coordinates.lon}" }

            // Display location data
            binding.stationGeohash.text = resources.getString(
                R.string.station_geohash,
                station.geohash
            )
            binding.stationLatitude.text = resources.getString(
                R.string.station_latitude,
                coordinates.lat.toString()
            )
            binding.stationLongitude.text = resources.getString(
                R.string.station_longitude,
                coordinates.lon.toString()
            )

            createLocalLocation()
        }else{
            // If no data was passed to the activity, end it
            setResult(RESULT_CANCELED)
            finish()
        }

        // Get forecast button
        binding.stationBtnForecast.setOnClickListener {
            val intent = Intent(this, LocationDetailsActivity::class.java)
            intent.putExtra("location", stationAsLocation)
            startActivityForResult(intent, 0)
        }

        // Add as location button
        binding.stationBtnAddAsLoc.setOnClickListener {
            app.locations.add(stationAsLocation)
            toast(resources.getString(R.string.location_added, station.location))
            binding.stationBtnAddAsLoc.isEnabled = false
        }

        // Create a marker and symbol layer for the map
        val symbolLayers = ArrayList<Feature>()
        symbolLayers.add(Feature.fromGeometry(Point.fromLngLat(coordinates.lon, coordinates.lat))) //remember to reverse LatLng

        // Set map theme depending of device display mode
        var mapTheme = Style.MAPBOX_STREETS
        if(isDarkModeOn()) mapTheme = Style.DARK

        // Configure the map style
        val mapStyle = Style.Builder().fromUri(mapTheme).withImage("ICON_ID", BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.mapbox_marker_icon_default))!!)
            .withSource(GeoJsonSource("SOURCE_ID", FeatureCollection.fromFeatures(symbolLayers))).withLayer(SymbolLayer("LAYER_ID", "SOURCE_ID")
                .withProperties(iconImage("ICON_ID"), iconSize(1.0f), iconAllowOverlap(true), iconIgnorePlacement(true)))

        // Load the map
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync {
                mapboxMap -> mapboxMap.setStyle(mapStyle) {
                    // Center map on station location
                    val position = CameraPosition.Builder()
                        .target(LatLng(coordinates.lat, coordinates.lon))
                        .zoom(10.0).tilt(0.0).build()
                    // Pan the map to the new location
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 2000)

            }

            this.mapboxMap = mapboxMap

            // Zoom in button
            binding.observationStationBtnZoomIn.setOnClickListener {
                changeMapPosition(zoom=mapboxMap.cameraPosition.zoom + 1, duration = 500)
            }

            // Zoom out button
            binding.observationStationBtnZoomOut.setOnClickListener {
                changeMapPosition(zoom=mapboxMap.cameraPosition.zoom - 1, duration = 500)
            }
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
     * Check if the device is running in dark mode or not
     */
    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        createLocalLocation()
    }

    /**
     * Helper function to change the map position and zoom level
     *
     * @param lat the desired latitude (default: current map latitude)
     * @param lon the desired longitude (default: current map longitude)
     * @param zoom the desired zoom level (default: 9.0)
     * @param duration the length of time in ms to pan the map to the new location (default: 2000)
     */
    private fun changeMapPosition(lat: Double = mapboxMap.cameraPosition.target.latitude, lon: Double = mapboxMap.cameraPosition.target.longitude, zoom: Double = 9.0, duration: Int = 2000){
        val position = CameraPosition.Builder().target(LatLng(lat, lon)).zoom(zoom).tilt(0.0).build()
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), duration)
    }

    private fun createLocalLocation(){
        // Disable the add as location button if a saved location already
        // exists with the same name
        if (app.locations.findOneByName(station.location) != null){
            binding.stationBtnAddAsLoc.isEnabled = false

            // Get existing location from list
            stationAsLocation = app.locations.findOneByName(station.location)!!
        }else{
            // Create a location object for the station
            stationAsLocation = Location(-1, station.location, coordinates.lat, coordinates.lon, 0)
        }
    }
}