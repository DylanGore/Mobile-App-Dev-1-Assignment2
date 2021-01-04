package ie.dylangore.mad1.assignment2

import ie.dylangore.mad1.assignment2.helpers.RequestHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Test the observation stations parser
 */
class ObservationStationsTest {

    /**
     * Test that the HTTP request for observation stations returns data and that
     * the data can be parsed correctly
     */
    @Test
    fun testStationsParserRequestAndParse() {
        Assertions.assertNotNull(RequestHelper.getMetEireannStationsSync())
    }
}