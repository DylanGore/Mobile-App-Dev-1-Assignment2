package ie.dylangore.mad1.assignment2.main

import android.app.Application
import ie.dylangore.mad1.assignment2.helpers.RequestHelper
import ie.dylangore.mad1.assignment2.models.Location
import ie.dylangore.mad1.assignment2.models.ObservationStation
import ie.dylangore.mad1.assignment2.models.Warning
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class MainApp : Application(), AnkoLogger {

    var stations: ArrayList<ObservationStation.ObservationStationItem> = arrayListOf()
    var warnings: ArrayList<Warning.WarningItem> = arrayListOf()
    var locations: ArrayList<Location> = arrayListOf()

    override fun onCreate() {
        super.onCreate()
        info("App has started!")
        updateStations()
        updateWarnings()
        locations.add(Location(1, "test"))
    }

    private fun updateStations() {
        stations = RequestHelper.getMetEireannStations()
    }

    private fun updateWarnings() {
        warnings = RequestHelper.getWeatherWarnings()
    }
}