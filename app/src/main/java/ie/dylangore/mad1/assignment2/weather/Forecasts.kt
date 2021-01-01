package ie.dylangore.mad1.assignment2.weather

import com.beust.klaxon.Klaxon
import ie.dylangore.mad1.assignment2.models.Forecast
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception

/**
 * Object that handles getting and formatting forecast data
 */
object Forecasts {
    // Create the HTTP client used to communicate with the API
    private val client = OkHttpClient()

    /**
     * Make a HTTP request to the Met.No forecast API to get weather forecast data in JSON
     * format for a given location
     *
     * @param baseUrl the API base URL without any arguments
     * @param latitude the latitude of the location
     * @param longitude the longitude of the location
     * @param altitude the altitude of the location
     * @return the forecast data as a JSON string
     */
    private fun apiRequest(baseUrl: String = "https://api.met.no/weatherapi/locationforecast/2.0/compact", latitude:Double = 52.2461, longitude:Double = -7.1387, altitude: Int = 0): String {
        val request = Request.Builder().url("$baseUrl?altitude=$altitude&lat=$latitude&lon=$longitude").header("User-Agent", "KotlinWeather").build()
        var result = "[]"

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                result = response.body!!.string()
            }
        }

        return(result)
    }

    /**
     * Convert the forecast JSON from Met.No to the Forecast data object
     *
     * @param latitude the latitude of the location
     * @param longitude the longitude of the location
     * @param altitude the altitude of the location (default: 0)
     * @param data the forecast data passed as a string, used for testing (default: empty)
     * @return the weather forecast for the given location as a Forecast object
     */
    fun getForecast(latitude:Double = 52.2461, longitude:Double = -7.1387, altitude: Int = 0, data:String = ""): Forecast? {
        // Check if data has been passed as a string, if not get new data from the API
        val forecastData: String = if (data.isEmpty()) apiRequest(latitude = latitude, longitude = longitude, altitude = altitude) else data
        // Parse the data and return it as a Forecast object
        return try{
            Klaxon().parse(forecastData)
        }catch (ex: Exception){
            error("Error while parsing forecast data")
            null
        }
    }
}