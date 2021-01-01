package ie.dylangore.mad1.assignment2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.ObservationStation
import org.jetbrains.anko.AnkoLogger

class ObservationStationsFragment : Fragment(), AnkoLogger {

    lateinit var app: MainApp;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = requireActivity().application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root : FrameLayout = inflater.inflate(R.layout.fragment_observation_stations, container, false) as FrameLayout
        val layoutManager = LinearLayoutManager(this.context)
        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_observation_stations)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = StationAdapter(app.stations)

        // Inflate the layout for this fragment
        return root
    }
}

private class StationAdapter(private var stations: ArrayList<ObservationStation.ObservationStationItem>) : RecyclerView.Adapter<StationAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationAdapter.MainHolder {
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
            var cardTitle : TextView = itemView.findViewById(R.id.list_card_title)
            cardTitle.text = station.location
        }
    }
}