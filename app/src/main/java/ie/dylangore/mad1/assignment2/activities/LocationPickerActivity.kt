package ie.dylangore.mad1.assignment2.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import ie.dylangore.mad1.assignment2.BuildConfig
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.ActivityLocationPickerBinding
import ie.dylangore.mad1.assignment2.models.Location
import org.jetbrains.anko.AnkoLogger


/**
 * Activity that allows the user to pick a location from a map
 *
 */
class LocationPickerActivity : AppCompatActivity(), AnkoLogger, OnMapReadyCallback {

    private lateinit var binding: ActivityLocationPickerBinding
    private lateinit var location: Location


    /**
     * Runs when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, BuildConfig.MapboxAccessToken) // this must be done before the layout is inflated
        binding = ActivityLocationPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set title
        title = resources.getString(R.string.title_location_picker)

        // If there is location data, store it
        if (intent.hasExtra("location")) {
            location = intent.extras?.getParcelable("location")!!
        }

        // Initialise the map
        binding.locationPickerMap.onCreate(savedInstanceState)
        binding.locationPickerMap.getMapAsync(this)
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
     * Runs when the map has loaded
     *
     * @param mapboxMap the map view
     */
    override fun onMapReady(mapboxMap: MapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
            val uiSettings = mapboxMap.uiSettings

            // Disable tilt and rotate gestures as they are not required
            uiSettings.isTiltGesturesEnabled = false
            uiSettings.isRotateGesturesEnabled = false

            // Move MapBox attribution to top
            uiSettings.logoGravity = Gravity.TOP
            uiSettings.attributionGravity = Gravity.TOP

            // If there is an existing set of coordinates, center the map there
            if (location.latitude != 0.0 && location.longitude != 0.0){
                val position = CameraPosition.Builder()
                    .target(LatLng(location.latitude, location.longitude))
                    .zoom(9.0).tilt(0.0).build()
                // Pan the map to the new location
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 2000);
            }

            // Handle the button press, this is located here rather than directly in onCreate so the map loads first
            binding.locationPickerBtnConfirm.setOnClickListener {

                // Save the center point of the map
                location.latitude = mapboxMap.cameraPosition.target.latitude
                location.longitude = mapboxMap.cameraPosition.target.longitude

                // Return the data to the parent activity
                val intent = Intent()
                intent.putExtra("location", location)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}