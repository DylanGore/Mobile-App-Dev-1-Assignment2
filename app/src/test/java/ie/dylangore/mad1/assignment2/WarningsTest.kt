package ie.dylangore.mad1.assignment1.weather

import ie.dylangore.mad1.assignment2.helpers.RequestHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Test the weather warnings parser
 *
 */
class WarningsTest {

    /**
     * Test that the HTTP request for weather warnings returns data and that
     * the data can be parsed correctly (tested using the official API)
     *
     */
    @Test
    fun testWarningsRequestAndParse() {
        Assertions.assertNotNull(RequestHelper.getWeatherWarningsSync())
    }

    /**
     * Test that the HTTP request for weather warnings returns data and that
     * the data can be parsed correctly (tested using a test API)
     *
     */
    @Test
    fun testWarningsRequestAndParseTestAPI() {
        Assertions.assertNotNull(RequestHelper.getWeatherWarningsTestSync())
    }
}