package ie.dylangore.mad1.assignment2.services

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import ie.dylangore.mad1.assignment2.helpers.RequestHelper
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info

class WarningRequestService : JobIntentService(), AnkoLogger {

    override fun onHandleWork(intent: Intent) {
        val list = RequestHelper.getWeatherWarningsSync()
        debug("$list")

        val broadcastIntent = Intent()
        broadcastIntent.action = "ie.dylangore.weather"
        broadcastIntent.putExtra("warning", list)
        sendBroadcast(broadcastIntent)
        info("Sending warning broadcast")
    }

    companion object{
        fun enqueueWork(context: Context, intent: Intent){
            enqueueWork(context,WarningRequestService::class.java, 1, intent)
        }
    }
}