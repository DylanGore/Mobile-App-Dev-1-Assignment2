package ie.dylangore.mad1.assignment2.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.FragmentObservationStationsBinding
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.ObservationStation
import ie.dylangore.mad1.assignment2.services.StationRequestService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*
import kotlin.collections.ArrayList


/**
 * The fragment that displays the list of observation stations
 */
class ObservationStationsFragment : Fragment(), AnkoLogger, StationListener, SearchView.OnQueryTextListener {

    private lateinit var app: MainApp
    private lateinit var receiver: BroadcastReceiver
    private lateinit var adapter: StationAdapter

    // View binding
    private var _binding: FragmentObservationStationsBinding? = null
    private val binding get() = _binding!!

    /**
     * Called when fragment is initially created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = requireActivity().application as MainApp

        // Define a broadcast receiver to receive data from the background service that handles getting data from the internet
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.hasExtra("station")!!) {
                    val stations = intent.extras?.getParcelableArrayList<ObservationStation.ObservationStationItem>("station")
                    updateStationsList(stations as ArrayList<ObservationStation.ObservationStationItem>)
                    info("list: $stations")
                    info("Station Broadcast")
                    binding.layoutStationsRefresh.isRefreshing = false
                }
            }
        }

        // Register the broadcast receiver
        val intentFilter = IntentFilter()
        intentFilter.addAction("ie.dylangore.weather")
        activity?.registerReceiver(receiver, intentFilter)
    }

    /**
     * Runs when the view is created, any GUI-related init should be done here.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentObservationStationsBinding.inflate(inflater, container, false)

        // Show the refresh icon initially
        binding.layoutStationsRefresh.isRefreshing = true

        // Required to add search icon
        setHasOptionsMenu(true)

        // Update the RecyclerView initially with an empty list to avoid an error in the logs
        updateStationsList()

        // Get the latest stations from the internet
        refreshStations()

        // Handle the pull to refresh action
        binding.layoutStationsRefresh.setOnRefreshListener {
            refreshStations()
            Toast.makeText(this.context, "Refreshed Station List", Toast.LENGTH_SHORT).show()
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
     * Runs when the fragment is destroyed. The BroadcastReceiver
     * must be unregistered to avoid a memory leak.
     */
    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(receiver)
    }

    /**
     * Add the search view to the AppBar menu created by the parent activity
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        // Get the SearchView from the menu item
        val searchView = menu.findItem(R.id.menu_item_search).actionView as SearchView
        // Tell the search view to use the functions in this class to handle search box updates
        searchView.setOnQueryTextListener(this)
    }


    /**
     * Updates the displayed station list
     */
    private fun updateStationsList(stationsList: ArrayList<ObservationStation.ObservationStationItem> = arrayListOf()) {
        val recyclerView = binding.recyclerViewObservationStations
        val emptyLayout = binding.layoutEmptyObservationStations

        // Setup the RecyclerView and Adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        adapter = StationAdapter(stationsList, this)
        recyclerView.adapter = adapter

        // Display a TextView if the list is empty
        if (stationsList.isNotEmpty()) {
            emptyLayout.visibility = View.GONE
        } else {
            emptyLayout.visibility = View.VISIBLE
        }
    }

    /**
     * Call a background service to send a GET request to update
     * the list of stations
     */
    private fun refreshStations() {
        val intent = Intent(this.activity, StationRequestService::class.java)
        StationRequestService.enqueueWork(this.requireContext(), intent)
    }

    /**
     * Opens the ObservationStationActivity when a station in the
     * list is clicked on.
     */
    override fun onStationClick(station: ObservationStation.ObservationStationItem) {
        val intent = Intent(activity, ie.dylangore.mad1.assignment2.activities.ObservationStationActivity::class.java)
        // Pass the selected station data through to the activity
        intent.putExtra("station_view", station)
        startActivityForResult(intent, 0)
    }

    /**
     * Called when the user presses enter while the search box is in focus
     *
     * @param query the contents of the search box
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    /**
     * Called whenever the contents of the search box change
     *
     * @param newText the new contents of the search box
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return false
    }
}
/**
 * Handle when a location list item is clicked on
 */
interface StationListener {
    fun onStationClick(station: ObservationStation.ObservationStationItem)
}


/**
 * Adapter to handle displaying a list of observation stations
 */
private class StationAdapter(private var stations: ArrayList<ObservationStation.ObservationStationItem>, private val listener: StationListener) : RecyclerView.Adapter<StationAdapter.MainHolder>(), Filterable {

    // Create a list to hold the results when the user enters something in the search box
    var stationsFiltered = ArrayList<ObservationStation.ObservationStationItem>()

    init {
        // Set the initially displayed list to the full list
        stationsFiltered = stations
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.list_card,
                parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val station = stationsFiltered[holder.adapterPosition]
        holder.bind(station, listener)
    }

    override fun getItemCount(): Int = stationsFiltered.size

    private class MainHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(station: ObservationStation.ObservationStationItem, listener: StationListener){
            itemView.setOnClickListener { listener.onStationClick(station) }
            val cardTitle : TextView = itemView.findViewById(R.id.list_card_title)
            cardTitle.text = station.location
        }
    }

    /**
     * Create the search logic
     *
     * @return a set of resulting objects that match the user's search
     */
    override fun getFilter(): Filter {
        return object : Filter() {
            @Suppress("LiftReturnOrAssignment")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchText = constraint.toString()

                // If there is nothing in the search bar, show the full list
                if (searchText.isEmpty()){
                    stationsFiltered = stations
                } else {
                    val results = ArrayList<ObservationStation.ObservationStationItem>()
                    for (station in stations){
                        if (station.location.toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(Locale.ROOT))){
                            results.add(station)
                        }
                    }
                    stationsFiltered = results
                }
                // Create a FilterResults object to return
                val filterResults = FilterResults()
                filterResults.values = stationsFiltered
                return filterResults
            }

            /**
             * Update the list that is displayed to the user with the newly filtered list
             *
             * @param constraint the contents of the search box
             * @param results the new list
             */
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                println("constraint: $constraint")
                stationsFiltered = results?.values as ArrayList<ObservationStation.ObservationStationItem>
                // Update the UI
                notifyDataSetChanged()
            }

        }
    }
}