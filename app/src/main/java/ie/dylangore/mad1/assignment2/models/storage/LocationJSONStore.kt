package ie.dylangore.mad1.assignment2.models.storage

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ie.dylangore.mad1.assignment2.helpers.FileHelper
import ie.dylangore.mad1.assignment2.helpers.ValidationHelper
import ie.dylangore.mad1.assignment2.models.Location
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.lang.reflect.Type

/**
 * Class that manages storing location data in memory
 *
 * @property context Android application context
 */
class LocationJSONStore(private val context: Context, private val jsonFile: String = "locations.json"): LocationStore, AnkoLogger {

    private val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting().create()
    private val listType: Type = object : TypeToken<java.util.ArrayList<Location>>() {}.type

    private var locations: ArrayList<Location> = ArrayList()

    /**
     * The primary constructor - runs when the class is initialised
     */
    init {
        // If a JSON file exists, import the data from it
        if (FileHelper.exists(context, jsonFile)) {
            deserialize()
        }
    }

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
     * Add a new location TODO: validation
     *
     * @param location the new location to add
     */
    override fun add(location: Location) {
        location.id = getId()
        if (ValidationHelper.validateLocation(location)){
            locations.add(location)
            serialize()
            info("New location added: $location")
        }else{
            error( "Location not added: invalid location data")
        }
    }

    /**
     * Update an existing location
     *
     * @param location the location to update
     */
    override fun update(location: Location) {
        val foundLocation = findOne(location.id)
        if (foundLocation != null) {
            info("Updating location: $location")
            foundLocation.name = location.name
            foundLocation.latitude = location.latitude
            foundLocation.longitude = location.longitude
            foundLocation.altitude = location.altitude
            // Validate the updated location
            if (ValidationHelper.validateLocation(foundLocation)){
            // Save the updated list to the JSON file
                serialize()
            }
            else{
                error( "Location with ID ${location.id} not added: invalid location data")
           }
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
            // Save the updated list to the JSON file
            serialize()
        }else{
            error("Location with ID $id not found, nothing to delete")
        }
    }

    /**
     * Delete all entries from the list
     */
    override fun empty() {
        locations.removeAll(locations)
        serialize()
    }

    /**
     * Get the next available element ID
     *
     * @return the new ID as a Long
     */
    private fun getId(): Long{
        return if (locations.isNotEmpty()) locations.last().id + 1 else 1
    }

    /**
     * Write the current version of the locations list to a JSON file
     */
    private fun serialize() {
        // Convert the existing list to a JSON String
        val jsonString = gsonBuilder.toJson(locations, listType)
        // Write the JSON data to the file
        FileHelper.write(context, jsonFile, jsonString)
    }

    /**
     * Read the JSON file and replace the current contents of the locations list with
     * the data from the file
     */
    private fun deserialize() {
        // Get the JSON data as a String
        val jsonString = FileHelper.read(context, jsonFile)
        // Convert the JSON string to a list and replace the existing list
        locations = Gson().fromJson(jsonString, listType)
    }

}