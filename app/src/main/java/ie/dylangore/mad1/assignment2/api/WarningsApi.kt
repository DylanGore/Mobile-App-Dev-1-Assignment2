package ie.dylangore.mad1.assignment2.api

import ie.dylangore.mad1.assignment2.models.Warning
import retrofit2.Call
import retrofit2.http.GET

interface WarningsApi {
    @GET("warning_IRELAND.json")
    fun getWeatherWarnings(): Call<ArrayList<Warning.WarningItem>>
}