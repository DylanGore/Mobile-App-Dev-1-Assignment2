package ie.dylangore.mad1.assignment2

import ie.dylangore.mad1.assignment2.helpers.RequestHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ForecastTest {
    /**
     * Test that the HTTP request to the Met.No API returns data and that the data can
     * be parsed correctly
     *
     */
    @Test
    fun testForecastRequestAndParse() {
        // Attempt to request and parse a forecast for Waterford, Ireland
        Assertions.assertNotNull(RequestHelper.getForecastSync(13,52.2583300, -7.1119400,))
    }
}