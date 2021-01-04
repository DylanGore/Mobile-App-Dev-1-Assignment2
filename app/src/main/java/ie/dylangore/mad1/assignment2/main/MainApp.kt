package ie.dylangore.mad1.assignment2.main

import android.app.Application
import ie.dylangore.mad1.assignment2.models.ObservationStation
import ie.dylangore.mad1.assignment2.models.Warning
import ie.dylangore.mad1.assignment2.models.storage.LocationJSONStore
import ie.dylangore.mad1.assignment2.models.storage.LocationStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * The main application class
 */
class MainApp : Application(), AnkoLogger {

    lateinit var locations: LocationStore
    var useWarningTestAPI = false

    /**
     * Runs when the app is started
     */
    override fun onCreate() {
        super.onCreate()
        info("App has started!")

        // Initialize the location store
        locations = LocationJSONStore(applicationContext)
    }
}