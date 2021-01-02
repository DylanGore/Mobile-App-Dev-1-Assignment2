@file:Suppress("ClassName")

package ie.dylangore.mad1.assignment2.models

import com.google.gson.annotations.SerializedName

/**
 * Forecast data class for the Met.no Forecast API
 *
 * @property geometry contains the location coordinates and altitude
 * @property properties contains all weather forecast information and metadata
 * @property type should always be "Feature", irrelevant for this use case
 */
data class Forecast(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
) {
    /**
     * Contains the location coordinates and altitude
     *
     * @property type should always be point
     * @property coordinates a list of coordinates
     */
    data class Geometry (
        val type: String,
        val coordinates: List<Double>
    )

    /**
     * Contains all weather forecast information and metadata
     *
     * @property meta forecast metadata
     * @property timeseries forecast data
     */
    data class Properties (
        val meta: Meta,
        val timeseries: List<TimeSeries>
    )

    /**
     * Contains forecast metadata
     *
     * @property updatedAt when the forecast model was last updated
     * @property units the units used in the forecast data
     */
    data class Meta (
        @SerializedName("updated_at")
        val updatedAt: String,

        val units: Units
    )

    /**
     * Describes the units used in the weather forecast
     *
     * @property airPressureAtSeaLevel - as named
     * @property airTemperature - as named
     * @property cloudAreaFraction - as named
     * @property precipitationAmount - as named
     * @property relativeHumidity - as named
     * @property windFromDirection - as named
     * @property windSpeed - as named
     */
    data class Units (
        @SerializedName("air_pressure_at_sea_level")
        val airPressureAtSeaLevel: String,

        @SerializedName("air_temperature")
        val airTemperature: String,

        @SerializedName("cloud_area_fraction")
        val cloudAreaFraction: String,

        @SerializedName("precipitation_amount")
        val precipitationAmount: String,

        @SerializedName("relative_humidity")
        val relativeHumidity: String,

        @SerializedName("wind_from_direction")
        val windFromDirection: String,

        @SerializedName("wind_speed")
        val windSpeed: String
    )

    /**
     * Contains forecast data for a given time
     *
     * @property time the time of the forecast
     * @property data the data for this time
     */
    data class TimeSeries (
        val time: String,
        val data: Data
    )

    /**
     * Forecast data
     *
     * @property instant forecast data right now
     * @property next12_Hours forecast data in the next 12 hours
     * @property next1_Hours forecast data in the next 1 hour
     * @property next6_Hours forecast data in the next 6 hours
     */
    data class Data (
        val instant: Instant,

        @SerializedName("next_12_hours")
        val next12_Hours: Next12_Hours? = null,

        @SerializedName("next_1_hours")
        val next1_Hours: NextHours? = null,

        @SerializedName("next_6_hours")
        val next6_Hours: NextHours? = null
    )

    /**
     * The current forecast data
     *
     * @property details the specific data
     */
    data class Instant (
        val details: InstantDetails
    )

    /**
     * Specific forecast measurements
     *
     * @property airPressureAtSeaLevel
     * @property airTemperature
     * @property cloudAreaFraction
     * @property relativeHumidity
     * @property windFromDirection
     * @property windSpeed
     */
    data class InstantDetails (
        @SerializedName("air_pressure_at_sea_level")
        val airPressureAtSeaLevel: Double,

        @SerializedName("air_temperature")
        val airTemperature: Double,

        @SerializedName("cloud_area_fraction")
        val cloudAreaFraction: Double,

        @SerializedName("relative_humidity")
        val relativeHumidity: Double,

        @SerializedName("wind_from_direction")
        val windFromDirection: Double,

        @SerializedName("wind_speed")
        val windSpeed: Double
    )

    /**
     * Forecast summary for the next 12 hours
     *
     * @property summary
     */
    data class Next12_Hours (
        val summary: Summary
    )

    /**
     * Forecast summary
     *
     * @property symbolCode the weather symbol to use
     */
    data class Summary (
        @SerializedName("symbol_code")
        val symbolCode: String
    )

    /**
     * Forecast summary
     *
     * @property summary the weather symbol to use
     * @property details the forecasted rain details
     */
    data class NextHours (
        val summary: Summary,
        val details: Next1_HoursDetails
    )

    /**
     * Describes the amount of forecasted rain in the next hour
     *
     * @property precipitationAmount amount of rain
     */
    data class Next1_HoursDetails (
        @SerializedName("precipitation_amount")
        val precipitationAmount: Double
    )

}