@file:Suppress("ClassName")

package ie.dylangore.mad1.assignment2.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Forecast data class for the Met.no Forecast API
 *
 * @property geometry contains the location coordinates and altitude
 * @property properties contains all weather forecast information and metadata
 * @property type should always be "Feature", irrelevant for this use case
 */
@Parcelize
data class Forecast(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
) : Parcelable {
    /**
     * Contains the location coordinates and altitude
     *
     * @property type should always be point
     * @property coordinates a list of coordinates
     */
    @Parcelize
    data class Geometry (
        val type: String,
        val coordinates: List<Double>
    ) : Parcelable

    /**
     * Contains all weather forecast information and metadata
     *
     * @property meta forecast metadata
     * @property timeseries forecast data
     */
    @Parcelize
    data class Properties (
        val meta: Meta,
        val timeseries: List<TimeSeries>
    ) : Parcelable

    /**
     * Contains forecast metadata
     *
     * @property updatedAt when the forecast model was last updated
     * @property units the units used in the forecast data
     */
    @Parcelize
    data class Meta (
        @SerializedName("updated_at")
        val updatedAt: String,

        val units: Units
    ) : Parcelable

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
    @Parcelize
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
    ) : Parcelable

    /**
     * Contains forecast data for a given time
     *
     * @property time the time of the forecast
     * @property data the data for this time
     */
    @Parcelize
    data class TimeSeries (
        val time: String,
        val data: Data
    ) : Parcelable

    /**
     * Forecast data
     *
     * @property instant forecast data right now
     * @property next12_Hours forecast data in the next 12 hours
     * @property next1_Hours forecast data in the next 1 hour
     * @property next6_Hours forecast data in the next 6 hours
     */
    @Parcelize
    data class Data (
        val instant: Instant,

        @SerializedName("next_12_hours")
        val next12_Hours: Next12_Hours? = null,

        @SerializedName("next_1_hours")
        val next1_Hours: NextHours? = null,

        @SerializedName("next_6_hours")
        val next6_Hours: NextHours? = null
    ) : Parcelable

    /**
     * The current forecast data
     *
     * @property details the specific data
     */
    @Parcelize
    data class Instant (
        val details: InstantDetails
    ) : Parcelable

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
    @Parcelize
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
    ) : Parcelable

    /**
     * Forecast summary for the next 12 hours
     *
     * @property summary
     */
    @Parcelize
    data class Next12_Hours (
        val summary: Summary
    ) : Parcelable

    /**
     * Forecast summary
     *
     * @property symbolCode the weather symbol to use
     */
    @Parcelize
    data class Summary (
        @SerializedName("symbol_code")
        val symbolCode: String
    ) : Parcelable

    /**
     * Forecast summary
     *
     * @property summary the weather symbol to use
     * @property details the forecast rain details
     */
    @Parcelize
    data class NextHours (
        val summary: Summary,
        val details: Next1_HoursDetails
    ) : Parcelable

    /**
     * Describes the amount of forecast rain in the next hour
     *
     * @property precipitationAmount amount of rain
     */
    @Parcelize
    data class Next1_HoursDetails (
        @SerializedName("precipitation_amount")
        val precipitationAmount: Double
    ) : Parcelable

}