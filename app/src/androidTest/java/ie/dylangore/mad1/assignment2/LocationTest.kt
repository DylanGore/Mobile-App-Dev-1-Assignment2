package ie.dylangore.mad1.assignment2

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import ie.dylangore.mad1.assignment2.models.storage.LocationJSONStore
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LocationTest {



    @Test
    fun testAddLocation(){
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        val location = LocationJSONStore(appContext)
        // check that list is currently empty
        Assertions.assertEquals(0, location.findAll().size)
    }

}