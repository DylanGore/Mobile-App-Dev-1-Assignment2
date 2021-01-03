package ie.dylangore.mad1.assignment2.services

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import ie.dylangore.mad1.assignment2.helpers.RequestHelper
import ie.dylangore.mad1.assignment2.models.Location
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info
import org.jetbrains.anko.warn

/**
 * A background service that handles requesting weather forecast data from the API
 */
class ForecastRequestService: JobIntentService(), AnkoLogger {

    private lateinit var location: Location

    /**
     * Runs when the service is called
     */
    override fun onHandleWork(intent: Intent) {

        if (intent.hasExtra("location")) {
            location = intent.extras?.getParcelable("location")!!
            info { "Service received location $location" }
        }

        // Request the data from the API
        val forecast = RequestHelper.getForecastSync(location.altitude, location.latitude, location.longitude)
        info("Forecast API Response: $forecast")

        // Create a broadcast intent to send the data
        val broadcastIntent = Intent()
        broadcastIntent.action = "ie.dylangore.weather"

        // If forecast data is not null, attach it to the broadcast
        if (forecast !== null){
            broadcastIntent.putExtra("forecast", forecast)
        }else{
            warn("Forecast data is null")
            broadcastIntent.putExtra("forecast_none", 0)
        }

        // Send broadcast
        sendBroadcast(broadcastIntent)
        info("Sending forecast broadcast")
    }

    companion object{
        /**
         * Used to call the service
         */
        fun enqueueWork(context: Context, intent: Intent){
            enqueueWork(context,ForecastRequestService::class.java, 1, intent)
        }
    }
}