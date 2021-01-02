package ie.dylangore.mad1.assignment2.api

import ie.dylangore.mad1.assignment2.models.Forecast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface to list API endpoints and results for weather forecasts
 */
interface ForecastApi {
    /**
     * /compact - return a Forecast object
     */
    @GET("compact")
    fun getForecast(@Query("altitude") altitude: String,
                    @Query("lat") latitude: String,
                    @Query("lon") longitude: String): Call<Forecast>
}