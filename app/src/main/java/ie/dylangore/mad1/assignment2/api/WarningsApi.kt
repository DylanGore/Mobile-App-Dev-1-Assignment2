package ie.dylangore.mad1.assignment2.api

import ie.dylangore.mad1.assignment2.models.Warning
import retrofit2.Call
import retrofit2.http.GET

/**
 * Interface to list API endpoints and results for Met Éireann weather warnings
 */
interface WarningsApi {
    /**
     * Return an array list of WarningItem objects
     */
    @GET("warning_IRELAND.json")
    fun getWeatherWarnings(): Call<ArrayList<Warning.WarningItem>>
}