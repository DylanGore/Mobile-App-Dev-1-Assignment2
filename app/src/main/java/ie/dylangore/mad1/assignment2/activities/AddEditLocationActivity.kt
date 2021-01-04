package ie.dylangore.mad1.assignment2.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.ActivityAddEditLocationBinding
import ie.dylangore.mad1.assignment2.helpers.ValidationHelper
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.Location
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

/**
 * The activity used to add/edit a location
 */
class AddEditLocationActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var app: MainApp
    private lateinit var binding: ActivityAddEditLocationBinding
    private var location = Location()
    private var editMode = false

    /**
     * Runs when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityAddEditLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("location_edit")) {
            editMode = true
            location = intent.extras?.getParcelable("location_edit")!!
            binding.locationName.setText(location.name)
            binding.locationLatitude.setText(location.latitude.toString())
            binding.locationLongitude.setText(location.longitude.toString())
            binding.locationAltitude.setText(location.altitude.toString())

            // Update the title and button text
            title = "${resources.getString(R.string.title_edit_location)}: ${location.name} (ID ${location.id})"
            binding.locationAddEditButton.setText(R.string.location_edit_button_text)
        }

        // Add/Edit Button
        binding.locationAddEditButton.setOnClickListener {

            var invalid = false

            // Validate name
            if (ValidationHelper.validateName(binding.locationName.text.toString())){
                // If the name is valid, update the location object
                location.name = binding.locationName.text.toString()
            }else{
                // Show error if invalid
                binding.locationName.error = resources.getString(R.string.error_invalid_name)
                invalid = true
            }

            // Validate latitude
            try {
                // Get the value currently in the TextView
                val value = binding.locationLatitude.text.toString().toDouble()

                // If it's valid, update the location object
                if (ValidationHelper.validateLatitude(value)){
                    location.latitude = value
                }else{
                    // Show error if invalid
                    binding.locationLatitude.error = resources.getString(R.string.error_invalid_latitude)
                    invalid = true
                }

            } catch (ex: NumberFormatException){
                // Exception should only be raised if no value is entered, show error
                binding.locationLatitude.error = resources.getString(R.string.error_empty)
                invalid = true
            }

            // Validate longitude
            try {
                // Get the value currently in the TextView
                val value = binding.locationLongitude.text.toString().toDouble()

                // If it's valid, update the location object
                if (ValidationHelper.validateLongitude(value)){
                    location.longitude = value
                }else{
                    // Show error if invalid
                    binding.locationLongitude.error = resources.getString(R.string.error_invalid_longitude)
                    invalid = true
                }

            } catch (ex: NumberFormatException){
                // Exception should only be raised if no value is entered, show error
                binding.locationLongitude.error = resources.getString(R.string.error_empty)
                invalid = true
            }

            // If altitude is empty, do not validate as it is optional
            if (binding.locationAltitude.text.toString().isNotBlank()){
                // Validate altitude
                try {
                    // Get the value currently in the TextView
                    val value = binding.locationAltitude.text.toString().toInt()

                    // If it's valid, update the location object
                    if (ValidationHelper.validateAltitude(value)){
                        location.altitude = value
                    }else{
                        // Show error if invalid
                        binding.locationAltitude.error = resources.getString(R.string.error_invalid_altitude)
                        invalid = true
                    }

                } catch (ex: NumberFormatException){
                    // Exception should only be raised if no value is entered, show error
                    binding.locationAltitude.error = resources.getString(R.string.error_empty)
                    invalid = true
                }
            }

            if (!invalid){
                if(editMode){
                    app.locations.update(location)
                    toast(resources.getString(R.string.updated, location.name))
                }else{
                    app.locations.add(location.copy())
                    toast(resources.getString(R.string.location_added, location.name))
                }

                // Close the activity
                setResult(RESULT_OK)
                finish()
            }else{
                toast(resources.getString(R.string.error_invalid_location))
            }


        }

        // Map Button
        binding.locationBtnLocationPicker.setOnClickListener {
            val intent = Intent(this, LocationPickerActivity::class.java)
            intent.putExtra("location", location)
            startActivityForResult(intent, 0)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(resultCode){
            RESULT_OK -> {
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")!!
                    binding.locationLatitude.setText(location.latitude.toString())
                    binding.locationLongitude.setText(location.longitude.toString())
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}