package ie.dylangore.mad1.assignment2.main

import android.app.Application
import ie.dylangore.mad1.assignment2.models.Location
import ie.dylangore.mad1.assignment2.models.storage.LocationJSONStore
import ie.dylangore.mad1.assignment2.models.storage.LocationStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * The main application class
 */
class MainApp : Application(), AnkoLogger {

    lateinit var locations: LocationStore

    /**
     * Runs when the app is started
     */
    override fun onCreate() {
        super.onCreate()
        info("App has started!")
        locations = LocationJSONStore(applicationContext)

        // Add a test location
//        locations.add(Location(0, "WIT", 52.2461, 7.1387, 0))
    }
}