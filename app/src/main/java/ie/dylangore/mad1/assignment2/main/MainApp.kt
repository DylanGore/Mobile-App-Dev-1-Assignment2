package ie.dylangore.mad1.assignment2.main

import android.app.Application
import ie.dylangore.mad1.assignment2.helpers.RequestHelper
import ie.dylangore.mad1.assignment2.models.ObservationStation
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class MainApp : Application(), AnkoLogger {

    var stations: ArrayList<ObservationStation.ObservationStationItem> = arrayListOf()

    override fun onCreate() {
        super.onCreate()
        info("App has started!")
        stations = RequestHelper.getMetEireannStations()
    }
}