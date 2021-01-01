package ie.dylangore.mad1.assignment2.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.ActivityAddLocationBinding
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.Location
import org.jetbrains.anko.toast

class AddLocationActivity : AppCompatActivity() {

    private lateinit var app: MainApp;
    private lateinit var binding: ActivityAddLocationBinding
    private var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityAddLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("location_edit")) {
            location = intent.extras?.getParcelable<Location>("location_edit")!!
            binding.addLocationName.setText(location.name)
            binding.addLocationLatitude.setText(location.latitude.toString())
            binding.addLocationLongitude.setText(location.longitude.toString())
            binding.addLocationAltitude.setText(location.altitude.toString())
        }

        binding.button.setOnClickListener {

            //TODO validation
            location.id = app.locations.last().id + 1
            location.name = binding.addLocationName.text.toString()
            location.latitude = binding.addLocationLatitude.text.toString().toDouble()
            location.longitude = binding.addLocationLongitude.text.toString().toDouble()
            location.altitude = binding.addLocationAltitude.text.toString().toInt()

            app.locations.add(location.copy())

            Toast.makeText(this, "Added ${location.name} to list!", Toast.LENGTH_SHORT).show()

            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_location, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_location_item_cancel -> {
                toast("Cancelled")
                setResult(RESULT_CANCELED)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}