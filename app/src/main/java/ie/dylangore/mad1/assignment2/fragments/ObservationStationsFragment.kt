package ie.dylangore.mad1.assignment2.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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

/**
 * The fragment that displays the list of observation stations
 */
class ObservationStationsFragment : Fragment(), AnkoLogger, StationListener {

    private lateinit var app: MainApp
    private lateinit var receiver : BroadcastReceiver

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
                if(intent?.hasExtra("station")!!){
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

        // Update the RecyclerView initially with an empty list to avoid an error in the logs
        updateStationsList()

        // Get the latest stations from the internet
        refreshStations()

        // Handle the pull to refresh action
        binding.layoutStationsRefresh.setOnRefreshListener {
            refreshStations()
            Toast.makeText(this.context,"Refreshed Station List", Toast.LENGTH_SHORT).show()
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
     * Updates the displayed station list
     */
    private fun updateStationsList(stationsList: ArrayList<ObservationStation.ObservationStationItem> = arrayListOf()){
        val recyclerView = binding.recyclerViewObservationStations
        val emptyLayout = binding.layoutEmptyObservationStations

        // Setup the RecyclerView and Adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = StationAdapter(stationsList, this)

        // Display a TextView if the list is empty
        if (stationsList.isNotEmpty()){
            emptyLayout.visibility = View.INVISIBLE
        }else{
            emptyLayout.visibility = View.VISIBLE
        }
    }

    /**
     * Call a background service to send a GET request to update
     * the list of stations
     */
    private fun refreshStations(){
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
private class StationAdapter(private var stations: ArrayList<ObservationStation.ObservationStationItem>, private val listener: StationListener) : RecyclerView.Adapter<StationAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.list_card,
                parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val station = stations[holder.adapterPosition]
        holder.bind(station, listener)
    }

    override fun getItemCount(): Int = stations.size

    private class MainHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(station: ObservationStation.ObservationStationItem, listener: StationListener){
            itemView.setOnClickListener { listener.onStationClick(station) }
            val cardTitle : TextView = itemView.findViewById(R.id.list_card_title)
            cardTitle.text = station.location
        }
    }
}