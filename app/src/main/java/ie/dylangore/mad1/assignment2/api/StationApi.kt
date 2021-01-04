package ie.dylangore.mad1.assignment2.api

import ie.dylangore.mad1.assignment2.models.ObservationStation
import retrofit2.Call
import retrofit2.http.GET

/**
 * Interface to list API endpoints and results for Observation Stations
 */
interface StationApi {
    /**
     * /meteireann - return an array list of ObservationStationItem objects
     */
    @GET("meteireann")
    fun getMetEireannStations(): Call<ArrayList<ObservationStation.ObservationStationItem>>
}