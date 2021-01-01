package ie.dylangore.mad1.assignment2.ui.fragments

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
import org.jetbrains.anko.AnkoLogger

class ObservationStationsFragment : Fragment(), AnkoLogger {

    private lateinit var app: MainApp
    private var _binding: FragmentObservationStationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = requireActivity().application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObservationStationsBinding.inflate(inflater, container, false)

        updateStations()

        // Swipe to refresh
        binding.layoutStationsRefresh.setOnRefreshListener {
            updateStations()
            binding.layoutStationsRefresh.isRefreshing = false
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

    private fun updateStations(){
        val recyclerView = binding.recyclerViewObservationStations
        val emptyLayout = binding.layoutEmptyObservationStations

        // Setup the RecyclerView and Adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = StationAdapter(app.stations)

        // Display a TextView if the list is empty
        if (app.stations.isNotEmpty()){
            emptyLayout.visibility = View.INVISIBLE
        }else{
            emptyLayout.visibility = View.VISIBLE
        }
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