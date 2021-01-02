package ie.dylangore.mad1.assignment2.services

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import ie.dylangore.mad1.assignment2.helpers.RequestHelper
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info

/**
 * A background service that handles requesting Observation Station data from the API
 */
class StationRequestService: JobIntentService(), AnkoLogger {

    /**
     * Runs when the service is called
     */
    override fun onHandleWork(intent: Intent) {
        // Request the data from the API
        val list = RequestHelper.getMetEireannStationsSync()
        debug("$list")

        // Create a broadcast intent to send the data
        val broadcastIntent = Intent()
        broadcastIntent.action = "ie.dylangore.weather"
        broadcastIntent.putExtra("station", list)
        sendBroadcast(broadcastIntent)
        info("Sending station broadcast")
    }

    companion object{
        /**
         * Used to call the service
         */
        fun enqueueWork(context: Context, intent: Intent){
            enqueueWork(context,StationRequestService::class.java, 1, intent)
        }
    }
}