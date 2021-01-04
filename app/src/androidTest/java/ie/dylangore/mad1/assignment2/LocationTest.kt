package ie.dylangore.mad1.assignment2

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ie.dylangore.mad1.assignment2.helpers.FileHelper
import ie.dylangore.mad1.assignment2.models.Location
import ie.dylangore.mad1.assignment2.models.storage.LocationJSONStore
import org.junit.AfterClass
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test the locations JSON store
 *
 */
@RunWith(AndroidJUnit4::class)
class LocationTest {

    /**
     * Clear the list before every test
     *
     */
    @Before
    fun setup(){
        // Ensure the list is empty
        locations.empty()
    }

    @Test
    fun testListEmpty(){
        assertEquals("Check list is empty",0, locations.findAll().size)
    }

    @Test
    fun testAddLocation(){
        assertNotNull("Add new location", locations.add(Location(-1, "Waterford, Ireland", 52.2583300, -7.1119400, 13)))
    }

    @Test
    fun testUpdateLocation(){
        // Get a location
        val existingLocation = Location(1, "New York, USA", 40.7142700, -74.0059700, 10)
        locations.add(existingLocation)
        // Change location information
        existingLocation.name = "NYC"
        existingLocation.latitude = 40.7142710
        existingLocation.altitude = 15
        // Update it in the store
        locations.update(existingLocation)
        // Confirm successful update
        println(locations.findAll())
        assertEquals("Check the the location updated correctly", existingLocation, locations.findOne(1))
    }

    @Test
    fun testDeleteLocation(){
        locations.add(Location(2, "Longyearbyen, Svalbard (Norway)", 78.2233400, 15.6468900, 1713))
        assertEquals("Ensure item was added", 1, locations.findAll().size)
        locations.delete(1)
        assertEquals("Ensure location was deleted", 0, locations.findAll().size)
        assertNull("Confirm location cannot be found", locations.findOne(1))
    }

    @Test
    fun testEmptyLocations(){
        // Add new locations
        locations.add(Location(-1, "Waterford, Ireland", 52.2583300, -7.1119400, 13))
        locations.add(Location(-1, "New York, USA", 40.7142700, -74.0059700, 10))
        locations.add(Location(-1, "Longyearbyen, Svalbard (Norway)", 78.2233400, 15.6468900, 1713))
        locations.add(Location(-1, "Sydney, Australia", -33.8678500, 151.2073200, 19))
        locations.add(Location(-1, "Tokyo, Japan", 35.6895000, 139.6917100, 40))

        assertEquals("Confirm locations have been added", 5, locations.findAll().size)

        // Empty the list
        locations.empty()

        assertEquals("Confirm list is now empty", 0, locations.findAll().size)
    }

    companion object {

        private var appContext: Context = ApplicationProvider.getApplicationContext()
        private val locations = LocationJSONStore(appContext, "locations-implementation-test.json")

        /**
         * Delete the JSON file used in testing once testing is done
         *
         */
        @AfterClass @JvmStatic
        fun cleanup(){
            FileHelper.delete(appContext, "locations-implementation-test.json")
        }
    }

}