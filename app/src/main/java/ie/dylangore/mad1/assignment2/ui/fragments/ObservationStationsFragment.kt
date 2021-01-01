package ie.dylangore.mad1.assignment2.ui.fragments

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
import ie.dylangore.mad1.assignment2.models.Warning
import ie.dylangore.mad1.assignment2.services.StationRequestService
import ie.dylangore.mad1.assignment2.services.WarningRequestService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class ObservationStationsFragment : Fragment(), AnkoLogger {

    private lateinit var app: MainApp
    lateinit var receiver : BroadcastReceiver
    private var _binding: FragmentObservationStationsBinding? = null
    private val binding get() = _binding!!

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

        // Get the latest stations
        refreshStations()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(receiver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObservationStationsBinding.inflate(inflater, container, false)

        // Update the RecyclerView initially with an empty list to avoid an error in the logs
        updateStationsList()

        // Swipe to refresh
        binding.layoutStationsRefresh.setOnRefreshListener {
            refreshStations()
            Toast.makeText(this.context,"Refreshed Station List", Toast.LENGTH_SHORT).show()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    /**
     * Required to fix possible memory leak when using view binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Updates the displayed station list
     */
    private fun updateStationsList(stationsList: ArrayList<ObservationStation.ObservationStationItem> = arrayListOf()){
        val recyclerView = binding.recyclerViewObservationStations
        val emptyLayout = binding.layoutEmptyObservationStations

        // Setup the RecyclerView and Adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = StationAdapter(stationsList)

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
}

private class StationAdapter(private var stations: ArrayList<ObservationStation.ObservationStationItem>) : RecyclerView.Adapter<StationAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.list_card,
                parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val station = stations[holder.adapterPosition]
        holder.bind(station)
    }

    override fun getItemCount(): Int = stations.size

    private class MainHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(station: ObservationStation.ObservationStationItem){
            val cardTitle : TextView = itemView.findViewById(R.id.list_card_title)
            cardTitle.text = station.location
        }
    }
}