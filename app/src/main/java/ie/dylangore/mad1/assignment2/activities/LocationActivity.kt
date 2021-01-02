package ie.dylangore.mad1.assignment2.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.ActivityLocationBinding
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.Location
import org.jetbrains.anko.toast

/**
 * The activity used to add/edit a location
 */
class LocationActivity : AppCompatActivity() {

    private lateinit var app: MainApp
    private lateinit var binding: ActivityLocationBinding
    private var location = Location()
    private var editMode = false

    /**
     * Runs when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("location_edit")) {
            editMode = true
            location = intent.extras?.getParcelable<Location>("location_edit")!!
            binding.locationName.setText(location.name)
            binding.locationLatitude.setText(location.latitude.toString())
            binding.locationLongitude.setText(location.longitude.toString())
            binding.locationAltitude.setText(location.altitude.toString())

            // Update the title and button text
            title = "${resources.getString(R.string.title_edit_location)}: ${location.name} (ID ${location.id})"
            binding.locationAddEditButton.setText(R.string.location_edit_button_text)

            // Show the delete button
            binding.locationDeleteButton.visibility = View.VISIBLE
        }

        // Add/Edit Button
        binding.locationAddEditButton.setOnClickListener {

            //TODO validation
            location.name = binding.locationName.text.toString()
            location.latitude = binding.locationLatitude.text.toString().toDouble()
            location.longitude = binding.locationLongitude.text.toString().toDouble()
            location.altitude = binding.locationAltitude.text.toString().toInt()

            if(editMode){
                app.locations.update(location)
                Toast.makeText(this, "Updated ${location.name}", Toast.LENGTH_SHORT).show()
            }else{
                app.locations.add(location.copy())
                Toast.makeText(this, "Added ${location.name} to list!", Toast.LENGTH_SHORT).show()
            }

            setResult(RESULT_OK)
            finish()
        }

        // Delete Button
        binding.locationDeleteButton.setOnClickListener {
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
    }

    /**
     * Create the top menu buttons
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_location, menu)
        return true
    }

    /**
     * Handle actions for the top menu buttons
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.location_item_cancel -> {
                toast("Cancelled")
                setResult(RESULT_CANCELED)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}