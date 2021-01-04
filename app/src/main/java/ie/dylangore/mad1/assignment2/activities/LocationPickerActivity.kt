package ie.dylangore.mad1.assignment2.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import org.jetbrains.anko.toast


/**
 * Activity that allows the user to pick a location from a map
 *
 */
class LocationPickerActivity : AppCompatActivity(), AnkoLogger, OnMapReadyCallback, LocationListener {

    private lateinit var binding: ActivityLocationPickerBinding
    private lateinit var locationData: Location
    private lateinit var locationManager: LocationManager
    private lateinit var mapboxMap: MapboxMap


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
            locationData = intent.extras?.getParcelable("location")!!
        }

        // Get device GPS location on button click
        binding.locationPickerBtnLocate.setOnClickListener {
            // Initialize the location manager
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            // Check if location permission is granted, if not, request it
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
            }else{
                // If permission is already granted, request a location update
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
            }
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
            this.mapboxMap = mapboxMap
            val uiSettings = mapboxMap.uiSettings

            // Disable tilt and rotate gestures as they are not required
            uiSettings.isTiltGesturesEnabled = false
            uiSettings.isRotateGesturesEnabled = false

            // Move MapBox attribution to top
            uiSettings.logoGravity = Gravity.TOP
            uiSettings.attributionGravity = Gravity.TOP

            // If there is an existing set of coordinates, center the map there
            if (locationData.latitude != 0.0 && locationData.longitude != 0.0){
                changeMapPosition(locationData.latitude, locationData.longitude)
            }

            // Handle the button press, this is located here rather than directly in onCreate so the map loads first
            binding.locationPickerBtnConfirm.setOnClickListener {

                // Save the center point of the map
                locationData.latitude = mapboxMap.cameraPosition.target.latitude
                locationData.longitude = mapboxMap.cameraPosition.target.longitude

                // Return the data to the parent activity
                val intent = Intent()
                intent.putExtra("location", locationData)
                setResult(RESULT_OK, intent)
                finish()
            }

            // Zoom in button
            binding.locationPickerBtnZoomIn.setOnClickListener {
                changeMapPosition(zoom=mapboxMap.cameraPosition.zoom + 1, duration = 500)
            }

            // Zoom out button
            binding.locationPickerBtnZoomOut.setOnClickListener {
                changeMapPosition(zoom=mapboxMap.cameraPosition.zoom - 1, duration = 500)
            }
        }
    }

    /**
     * Called when the device location changes
     *
     * @param location the new location
     */
    override fun onLocationChanged(location: android.location.Location) {
        changeMapPosition(location.latitude, location.longitude)
    }

    /**
     * Handle the result of a permission request
     *
     * @param requestCode the permission request code (0 in this case)
     * @param permissions array of the permissions that were requested
     * @param grantResults results of request
     */
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 0) {
            // If permission was granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
            }
            else {
                // If permission was denied
                toast(resources.getString(R.string.location_permission_denied))
            }
        }
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
}