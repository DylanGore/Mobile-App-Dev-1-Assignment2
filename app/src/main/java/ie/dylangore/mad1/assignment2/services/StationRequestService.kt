package ie.dylangore.mad1.assignment2.services

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import ie.dylangore.mad1.assignment2.helpers.RequestHelper
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info

class StationRequestService: JobIntentService(), AnkoLogger {
    override fun onHandleWork(intent: Intent) {
        val list = RequestHelper.getMetEireannStationsSync()
        debug("$list")

        val broadcastIntent = Intent()
        broadcastIntent.action = "ie.dylangore.weather"
        broadcastIntent.putExtra("station", list)
        sendBroadcast(broadcastIntent)
        info("Sending station broadcast")
    }

    companion object{
        fun enqueueWork(context: Context, intent: Intent){
            enqueueWork(context,StationRequestService::class.java, 1, intent)
        }
    }
}