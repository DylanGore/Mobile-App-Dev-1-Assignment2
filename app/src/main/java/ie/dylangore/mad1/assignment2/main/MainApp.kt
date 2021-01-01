package ie.dylangore.mad1.assignment2.main

import android.app.Application
import ie.dylangore.mad1.assignment2.helpers.RequestHelper
import ie.dylangore.mad1.assignment2.models.Location
import ie.dylangore.mad1.assignment2.models.ObservationStation
import ie.dylangore.mad1.assignment2.models.Warning
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class MainApp : Application(), AnkoLogger {

    var locations: ArrayList<Location> = arrayListOf()

    override fun onCreate() {
        super.onCreate()
        info("App has started!")
        locations.add(Location(1, "test"))
    }
}