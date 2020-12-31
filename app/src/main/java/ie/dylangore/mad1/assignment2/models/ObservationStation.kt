package ie.dylangore.mad1.assignment2.models

/**
 * Data class for a list of ObservationStations
 */
class ObservationStation : ArrayList<ObservationStation.ObservationStationItem>(){
    /**
     * Data class for an individual Met Éireann observation station
     * Some of these values can be null as the data provided by Met Éireann is often incomplete
     *
     * @property geohash the geohash of the station (decode to get lat and lon)
     * @property humidity the humidity value for the current hour
     * @property location the location (name) of the current station
     * @property pressure the pressure value for the current hour
     * @property rain_1h the rainfall value for the current hour
     * @property temperature the temperature value for the current hour
     * @property time the measurement time
     * @property weatherDescription the description of the current weather conditions
     * @property wind_direction_str the cardinal wind direction as a String (e.g. E, W NNW, etc.)
     * @property wind_gust the wind gust value for the current hour
     * @property wind_speed the wind speed value for the current hour
     */
    data class ObservationStationItem(
        val geohash: String,
        val humidity: Int?,
        val location: String,
        val pressure: Int?,
        val rain_1h: Double?,
        val temperature: Int?,
        val time: String,
        val weatherDescription: String?,
        val wind_direction_str: String?,
        val wind_gust: Int?,
        val wind_speed: Int?
    )
}