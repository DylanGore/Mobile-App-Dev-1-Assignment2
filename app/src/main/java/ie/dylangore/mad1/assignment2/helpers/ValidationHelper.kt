package ie.dylangore.mad1.assignment2.helpers

import ie.dylangore.mad1.assignment2.models.Location

/**
 * Functions relating to data validation
 */
object ValidationHelper {

    // Validation constraints
    private const val LATITUDE_MIN: Double = -90.0
    private const val LATITUDE_MAX: Double = 90.0
    private const val LONGITUDE_MIN: Double = -180.0
    private const val LONGITUDE_MAX: Double = 180.0
    private const val ALTITUDE_MAX: Int = 9000

    /**
     * Validate a location name
     *
     * @param name the name to validate
     * @return result
     */
    fun validateName(name: String): Boolean{
        if (name.length >= 3) return true
        return false
    }

    /**
     * Validate a latitude value
     *
     * @param latitude the latitude to validate
     * @return result
     */
    fun validateLatitude(latitude: Double): Boolean{
        if (latitude > LATITUDE_MIN && latitude < LATITUDE_MAX) return true
        return false
    }

    /**
     * Validate a longitude value
     *
     * @param longitude the longitude to validate
     * @return result
     */
    fun validateLongitude(longitude: Double): Boolean{
        if (longitude > LONGITUDE_MIN && longitude < LONGITUDE_MAX) return true
        return false
    }

    /**
     * Validate an altitude value
     *
     * @param altitude the altitude to validate
     * @return result
     */
    fun validateAltitude(altitude: Int): Boolean{
        if (altitude in 0..ALTITUDE_MAX) return true
        return false
    }

    /**
     * Validate each element in a location object using
     * the functions above
     *
     * @param location the location to validate
     * @return result
     */
    @Suppress("unused")
    fun validateLocation(location: Location): Boolean{
        if (validateName(location.name) && validateLatitude(location.latitude) &&
                validateLongitude(location.longitude) && validateAltitude(location.altitude)) return true
        return false
    }

}