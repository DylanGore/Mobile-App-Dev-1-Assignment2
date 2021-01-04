package ie.dylangore.mad1.assignment2.models.storage

import ie.dylangore.mad1.assignment2.models.Location
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Class that manages storing location data in memory
 *
 */
@Suppress("unused")
class LocationMemoryStore: LocationStore, AnkoLogger {

    private val locations: ArrayList<Location> = ArrayList()

    /**
     * Get a list of all stored locations
     *
     * @return a list of locations
     */
    override fun findAll(): ArrayList<Location> {
        return locations
    }

    /**
     * Get a single location by ID
     *
     * @param id the ID of the location to find
     * @return the Location object with the given ID
     */
    override fun findOne(id: Long): Location? {
        return locations.find { it.id == id}
    }

    /**
     * Get a single location by name
     *
     * @param name the name of the location to find
     * @return the Location object with the given name
     */
    override fun findOneByName(name: String): Location? {
        return locations.find { it.name.equals(name, ignoreCase = true) }
    }

    /**
     * Add a new location
     *
     * @param location the new location to add
     */
    override fun add(location: Location) {
        location.id = getId()
//        if (ValidationHelper.validateLocation(location)){
            locations.add(location)
            info{ "New location added: $location"}
//        }else{
//            error { "Location not added: invalid location data" }
//        }
    }

    /**
     * Update an existing location
     *
     * @param location the location to update
     */
    override fun update(location: Location) {
        val foundLocation = findOne(location.id)
        if (foundLocation != null){
            info("Updating location: $location")
            foundLocation.name = location.name
            foundLocation.latitude = location.latitude
            foundLocation.longitude = location.longitude
            foundLocation.altitude = location.altitude
        }else{
            error("Location with ID ${location.id} not found, nothing to update")
        }
    }

    /**
     * Delete a location
     *
     * @param id the ID of the location to delete
     */
    override fun delete(id: Long) {
        val location = findOne(id)
        if (location != null){
            info("Deleting location: $location")
            locations.remove(location)
        }else{
            error("Location with ID $id not found, nothing to delete")
        }
    }

    /**
     * Delete all entries from the list
     */
    override fun empty() {
        locations.removeAll(locations)
    }

    /**
     * Get the next available element ID
     *
     * @return the new ID as a Long
     */
    private fun getId(): Long{
        return if (locations.isNotEmpty()) locations.last().id + 1 else 1
    }

}