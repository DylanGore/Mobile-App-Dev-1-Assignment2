package ie.dylangore.mad1.assignment2.models.storage

import ie.dylangore.mad1.assignment2.models.Location

/**
 * Common interface for managing location data
 *
 */
interface LocationStore {
    /**
     * Get a list of all stored locations
     *
     * @return a list of locations
     */
    fun findAll(): List<Location>

    /**
     * Get a single location by ID
     *
     * @param id the ID of the location to find
     * @return the Location object with the given ID
     */
    fun findOne(id: Long): Location?

    /**
     * Add a new location
     *
     * @param location the new location to add
     */
    fun add(location: Location)

    /**
     * Update an existing location
     *
     * @param location the location to update
     */
    fun update(location: Location)

    /**
     * Delete a location
     *
     * @param id the ID of the location to delete
     */
    fun delete(id: Long)

    /**
     * Delete all entries from the list
     */
    fun empty()
}