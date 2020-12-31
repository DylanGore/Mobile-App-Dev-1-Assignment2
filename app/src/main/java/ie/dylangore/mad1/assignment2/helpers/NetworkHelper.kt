package ie.dylangore.mad1.assignment2.helpers

import android.content.Context
import android.net.ConnectivityManager

/**
 * Various network-related helper functions
 */
object NetworkHelper {
    /**
     * Simple function to check if the device is connected to the internet
     */
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return true
        }
        return false
    }
}