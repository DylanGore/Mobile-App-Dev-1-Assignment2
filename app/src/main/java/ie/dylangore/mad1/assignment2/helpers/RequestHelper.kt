package ie.dylangore.mad1.assignment2.helpers

import ie.dylangore.mad1.assignment2.api.StationApi
import ie.dylangore.mad1.assignment2.api.WarningsApi
import ie.dylangore.mad1.assignment2.models.ObservationStation
import ie.dylangore.mad1.assignment2.models.Warning
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Helper functions for requesting and processing data from the internet
 */
object RequestHelper: AnkoLogger {

    // Define the base URLs for each API
    private const val STATION_BASE_URL = "https://maps.stream.dylangore.space/api/latest/"
    private const val WARNING_BASE_URL = "https://www.met.ie/Open_Data/json/"


    /**
     * Synchronous function to get data from the observation station API
     */
    fun getMetEireannStationsSync(): ArrayList<ObservationStation.ObservationStationItem>{

        var result = mutableListOf<ObservationStation.ObservationStationItem>()

        val  retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(STATION_BASE_URL)
            .build()

        val stationApi = retrofit.create(StationApi::class.java)
        val call: Call<ArrayList<ObservationStation.ObservationStationItem>> = stationApi.getMetEireannStations()

        // Attempt to make a request to the API
        try {
            val response = call.execute()
            result = processStationResponse(response)
        } catch (t: Throwable){
            error{t.message}
        }

        info("Found ${result.size} stations")
        return result as ArrayList<ObservationStation.ObservationStationItem>
    }

    /**
     * Synchronous function to get data from the Met Éireann weather warning API
     */
    fun getWeatherWarningsSync(): ArrayList<Warning.WarningItem>{
        var result = mutableListOf<Warning.WarningItem>()
        val  retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(WARNING_BASE_URL)
            .build()

        val warningApi = retrofit.create(WarningsApi::class.java)
        val call: Call<ArrayList<Warning.WarningItem>> = warningApi.getWeatherWarnings()

        // Attempt to make a request to the API
        try {
            val response = call.execute()
            result = processWarningResponse(response)
        } catch (t: Throwable){
            error(t.message)
        }
        info("Found ${result.size} warnings")

        return  result as ArrayList<Warning.WarningItem>
    }

    /**
     * Process the received response from the weather warning API and return a list of weather warnings
     */
    private fun processWarningResponse(response:  Response<ArrayList<Warning.WarningItem>>): ArrayList<Warning.WarningItem> {
        val result = mutableListOf<Warning.WarningItem>()
        for (item in response.body()!!){
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
        return result as ArrayList<Warning.WarningItem>
    }

    /**
     * Process the received response from the station API and return a list of observation stations
     */
    private fun processStationResponse(response:  Response<ArrayList<ObservationStation.ObservationStationItem>>): ArrayList<ObservationStation.ObservationStationItem> {
        val result = mutableListOf<ObservationStation.ObservationStationItem>()
        for (station in response.body()!!){
            result.add(station)
        }
        return result as ArrayList<ObservationStation.ObservationStationItem>
    }

}