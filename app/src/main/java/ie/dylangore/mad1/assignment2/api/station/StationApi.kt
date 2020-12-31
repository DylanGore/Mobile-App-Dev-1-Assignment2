package ie.dylangore.mad1.assignment2.api.station

import ie.dylangore.mad1.assignment2.models.ObservationStation
import retrofit2.Call
import retrofit2.http.GET

interface StationApi {
    //https://maps.stream.dylangore.space/api/latest/meteireann

    @GET("meteireann")
    fun getMetEireannStations(): Call<ArrayList<ObservationStation.ObservationStationItem>>
}