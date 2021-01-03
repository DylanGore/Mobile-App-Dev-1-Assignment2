package ie.dylangore.mad1.assignment2.helpers

import android.content.Context
import android.util.Log
import java.io.*

/**
 * Helper functions relating to files
 */
object FileHelper {

    /**
     * Write data to a file
     *
     * @param context Android application context
     * @param fileName the file name to write to
     * @param data the data to write
     */
    fun write(context: Context, fileName: String, data: String) {
        try {
            val outputStreamWriter = OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: Exception) {
            Log.i("LocationJSONStore", "Cannot read file: $e")
        }
    }

    /**
     * Read data from a file
     *
     * @param context Android application context
     * @param fileName the file name to read from
     * @return the data read from the file
     */
    fun read(context: Context, fileName: String): String {
        var str = ""
        try {
            // Open the file
            val inputStream = context.openFileInput(fileName)
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                val partialStr = StringBuilder()
                // Attempt to read the file, line by line
                var done = false
                while (!done) {
                    val line = bufferedReader.readLine()
                    done = (line == null)
                    if (line != null) partialStr.append(line)
                }
                inputStream.close()
                str = partialStr.toString()
            }
        } catch (e: FileNotFoundException) {
            Log.e("LocationJSONStore", "file not found: $e")
        } catch (e: IOException) {
            Log.e("LocationJSONStore", "cannot read file: $e")
        }
        return str
    }

    /**
     * Check if a file exists
     *
     * @param context Android application context
     * @param fileName the file name to look for
     * @return boolean reflecting file state
     */
    fun exists(context: Context, filename: String): Boolean {
        val file = context.getFileStreamPath(filename)
        return file.exists()
    }

    /**
     * Delete a file TODO: test functionality
     *
     * @param context Android application context
     * @param fileName the name of the file to delete
     */
    fun delete(context: Context, fileName: String){
        // Delete the file if it exists
        if (exists(context, fileName)){
            val file = File(fileName)
            file.delete()
            Log.i("LocationJSONStore", "File $fileName deleted")
        }else{
            Log.i("LocationJSONStore", "File $fileName doesn't exist. Nothing to delete.")
        }
    }

}