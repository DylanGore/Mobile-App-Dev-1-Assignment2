package ie.dylangore.mad1.assignment2.main

import android.app.Application
import ie.dylangore.mad1.assignment2.models.Location
import ie.dylangore.mad1.assignment2.models.storage.LocationMemoryStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * The main application class
 */
class MainApp : Application(), AnkoLogger {

    val locations = LocationMemoryStore()

    /**
     * Runs when the app is started
     */
    override fun onCreate() {
        super.onCreate()
        info("App has started!")

        // Add a test location
        locations.add(Location(0, "test", 52.0, -7.256, 0))
    }
}