package ie.dylangore.mad1.assignment2.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.activities.AddEditLocationActivity
import ie.dylangore.mad1.assignment2.activities.LocationDetailsActivity
import ie.dylangore.mad1.assignment2.databinding.FragmentLocationsBinding
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.Location
import org.jetbrains.anko.AnkoLogger

/**
 * The fragment that displays the list of saved locations
 */
class LocationsFragment : Fragment(), LocationListener, AnkoLogger {

    private lateinit var app: MainApp

    // View binding
    private var _binding: FragmentLocationsBinding? = null
    private val binding get() = _binding!!

    /**
     * Called when fragment is initially created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = requireActivity().application as MainApp
    }

    /**
     * Runs when the view is created, any GUI-related init should be done here.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)

        // Set the layout of the RecyclerView
        binding.recyclerViewLocations.layoutManager = LinearLayoutManager(this.context)

        // Update the displayed locations list
        updateLocations()

        // Handle the Add Location FAB
        binding.fabAddLocation.setOnClickListener {
            // Start the LocationActivity
            val intent = Intent(activity, AddEditLocationActivity::class.java)
            startActivityForResult(intent, 0)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    /**
     * Runs when the view is destroyed. Required to avoid
     * possible memory leak when using view binding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Runs when an activity created within this fragment is closed
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Updated the displayed list
        binding.recyclerViewLocations.adapter?.notifyDataSetChanged()
        // Check if the list is now empty
        checkIfEmpty()
    }

    /**
     * Updates the displayed station list
     */
    private fun updateLocations(){
        val recyclerView = binding.recyclerViewLocations

        // Setup the RecyclerView and Adapter
        recyclerView.adapter = LocationAdapter(app.locations.findAll(), this)
        recyclerView.adapter?.notifyDataSetChanged()

        checkIfEmpty()
    }

    /**
     * Checks if the list of locations is empty. If it is,
     * display a view stating that.
     */
    private fun checkIfEmpty(){
        val emptyLayout = binding.layoutEmptyLocations
        // Display a TextView if the list is empty
        if (app.locations.findAll().isNotEmpty()){
            emptyLayout.visibility = View.INVISIBLE
        }else{
            emptyLayout.visibility = View.VISIBLE
        }
    }

    /**
     * Opens the LocationActivity in edit mode when a location in the
     * list is clicked on.
     */
    override fun onLocationClick(location: Location) {
        val intent = Intent(activity, LocationDetailsActivity::class.java)
        // Pass the selected location data through to the activity
        intent.putExtra("location", location)
        startActivityForResult(intent, 0)
    }
}

/**
 * Handle when a location list item is clicked on
 */
interface LocationListener {
    fun onLocationClick(location: Location)
}

/**
 * Adapter to handle displaying a list of locations
 */
private class LocationAdapter(private var locations: ArrayList<Location>, private val listener: LocationListener) : RecyclerView.Adapter<LocationAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.list_card,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val location = locations[holder.adapterPosition]
        holder.bind(location, listener)
    }

    override fun getItemCount(): Int = locations.size

    private class MainHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(location: Location, listener: LocationListener){
            itemView.setOnClickListener { listener.onLocationClick(location) }
            val cardTitle : TextView = itemView.findViewById(R.id.list_card_title)
            cardTitle.text = location.name
        }
    }
}