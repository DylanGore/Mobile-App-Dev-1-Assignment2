package ie.dylangore.mad1.assignment2.helpers

import android.annotation.SuppressLint
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import java.text.SimpleDateFormat
import java.util.*

/**
 * Functions to help with formatting timestamps
 */
object TimeHelper: AnkoLogger {

    /**
     * Convert an IS08601 timestamp string to a Date object
     *
     * @param timestampStr the string to attempt to parse
     * @return the Date object
     */
    @SuppressLint("SimpleDateFormat")
    fun fromISO8601(timestampStr: String): Date?{
        val timestampFormatMilli = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val timestampFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

        var date: Date? = null
        try{
            // Try to parse the String assuming it will include milliseconds
            date = timestampFormatMilli.parse(timestampStr)
        }catch (ex: Exception){
            try{
                // Try to parse the string without milliseconds
                date = timestampFormat.parse(timestampStr)
            }catch (ex: Exception){
                warn("Unable to convert $timestampStr to Date object")
            }
        }
        // Return the result
        return date
    }

}