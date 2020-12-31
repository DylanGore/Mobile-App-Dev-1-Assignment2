package ie.dylangore.mad1.assignment2.helpers

import ie.dylangore.mad1.assignment2.api.station.StationApi
import ie.dylangore.mad1.assignment2.models.ObservationStation
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestHelper: AnkoLogger {

    fun getMetEireannStations(): ArrayList<ObservationStation.ObservationStationItem>{

        var result = mutableListOf<ObservationStation.ObservationStationItem>()

        val  retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://maps.stream.dylangore.space/api/latest/")
            .build()

        val stationApi = retrofit.create(StationApi::class.java)
        val mcall: Call<ArrayList<ObservationStation.ObservationStationItem>> = stationApi.getMetEireannStations()
        mcall.enqueue(object : Callback<ArrayList<ObservationStation.ObservationStationItem>> {
            override fun onResponse(
                call: Call<ArrayList<ObservationStation.ObservationStationItem>>,
                response: Response<ArrayList<ObservationStation.ObservationStationItem>>
            ) {
                val stationList: ArrayList<ObservationStation.ObservationStationItem> =
                    response.body()!!
                for (station in stationList){
                    info { station }
                    result.add(station)
                }
            }

            override fun onFailure(
                call: Call<ArrayList<ObservationStation.ObservationStationItem>>,
                t: Throwable
            ) {
                error("HTTP Failure")
            }
        })
        return result as ArrayList<ObservationStation.ObservationStationItem>
    }

}