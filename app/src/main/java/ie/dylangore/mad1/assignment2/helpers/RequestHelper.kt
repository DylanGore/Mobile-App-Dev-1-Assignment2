package ie.dylangore.mad1.assignment2.helpers

import ie.dylangore.mad1.assignment2.api.StationApi
import ie.dylangore.mad1.assignment2.api.WarningsApi
import ie.dylangore.mad1.assignment2.models.ObservationStation
import ie.dylangore.mad1.assignment2.models.Warning
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestHelper: AnkoLogger {

    fun getMetEireannStations(): ArrayList<ObservationStation.ObservationStationItem>{

        val result = mutableListOf<ObservationStation.ObservationStationItem>()

        val  retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://maps.stream.dylangore.space/api/latest/")
            .build()

        val stationApi = retrofit.create(StationApi::class.java)
        val call: Call<ArrayList<ObservationStation.ObservationStationItem>> = stationApi.getMetEireannStations()
        try {
            call.enqueue(object : Callback<ArrayList<ObservationStation.ObservationStationItem>> {
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

                override fun onFailure(call: Call<ArrayList<ObservationStation.ObservationStationItem>>, t: Throwable) {
                    error("HTTP Failure")
                }
            })
        }catch (t: Throwable){
            error(t.message)
        }

        return result as ArrayList<ObservationStation.ObservationStationItem>
    }

    fun getWeatherWarnings(): ArrayList<Warning.WarningItem>{

        val result = mutableListOf<Warning.WarningItem>()

        val  retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl("https://www.met.ie/Open_Data/json/")
                .baseUrl("https://ha.home.dylangore.space/local/testing/")
                .build()

        val warningApi = retrofit.create(WarningsApi::class.java)
        val call: Call<ArrayList<Warning.WarningItem>> = warningApi.getWeatherWarnings()
        try {
            call.enqueue(object : Callback<ArrayList<Warning.WarningItem>> {
                override fun onResponse(
                        call: Call<ArrayList<Warning.WarningItem>>,
                        response: Response<ArrayList<Warning.WarningItem>>
                ) {
                    val warningList: ArrayList<Warning.WarningItem> = response.body()!!
                    for (item in warningList){
                        val regionList: List<String> = item.regions // Existing list
                        val newRegionList: MutableList<String> = mutableListOf()
                        // Loop through each existing region and add it's name to the new list
                        for (region in regionList){
                            if (WarningFormatHelper.regionMap.containsKey(region)){
                                newRegionList.add(WarningFormatHelper.regionMap[region].toString())
                            } else {
                                // Add the code if it isn't found in the map
                                newRegionList.add(region)
                            }
                        }

                        // If list contains all 26 counties, replace them with one value to represent the entire country
                        if (newRegionList.containsAll(WarningFormatHelper.allCounties)){
                            for ( county in WarningFormatHelper.allCounties){
                                newRegionList.remove(county)
                            }
                            newRegionList.add("Ireland")
                        }

                        // Set the regions for this warning to the new list
                        item.regions = newRegionList
                        info { item }
                        result.add(item)
                    }
                }

                override fun onFailure(call: Call<ArrayList<Warning.WarningItem>>, t: Throwable) {
                    error("HTTP Failure")
                }
            })
        }catch (t: Throwable){
            error(t.message)
        }
        return result as ArrayList<Warning.WarningItem>
    }

}