package ie.dylangore.mad1.assignment2.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.ActivityWarningBinding
import ie.dylangore.mad1.assignment2.helpers.WarningFormatHelper
import ie.dylangore.mad1.assignment2.models.Warning
import org.jetbrains.anko.AnkoLogger
import java.util.*

/**
 * The activity used to view details about a specific weather warning
 */
class WarningActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var binding: ActivityWarningBinding

    /**
     * Runs when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("warning_view")) {
            val warning : Warning.WarningItem = intent.extras?.getParcelable("warning_view")!!

            // Set the title to the warning headline
            title = warning.headline

            // Set icon color based on warning level
            val iconColor = WarningFormatHelper.getWarningIconColor(warning)

            binding.warningLevel.text = resources.getString(R.string.warning_level, warning.level.capitalize(Locale.ROOT))

            binding.warningIcon.setColorFilter(ContextCompat.getColor(this.applicationContext, iconColor), android.graphics.PorterDuff.Mode.SRC_IN)

            // Display warning info
            binding.warningDescription.text = warning.description
            binding.warningRegions.text = resources.getString(R.string.warning_regions, warning.regions.joinToString())
            binding.warningIssued.text = resources.getString(R.string.warning_issued, warning.issued)
            binding.warningUpdated.text = resources.getString(R.string.warning_updated, warning.updated)
            binding.warningOnset.text = resources.getString(R.string.warning_onset, warning.onset)
            binding.warningExpiry.text = resources.getString(R.string.warning_expiry, warning.expiry)
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