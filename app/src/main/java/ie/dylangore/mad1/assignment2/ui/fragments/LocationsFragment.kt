package ie.dylangore.mad1.assignment2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.Location
import org.jetbrains.anko.AnkoLogger

class LocationsFragment : Fragment(), AnkoLogger {

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
        // Get layout elements
        val root : FrameLayout = inflater.inflate(R.layout.fragment_locations, container, false) as FrameLayout
        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_locations)
        val emptyLayout: FrameLayout = root.findViewById(R.id.layout_empty_locations)

        // Setup the RecyclerView and Adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = LocationAdapter(app.locations)

        // Display a TextView if the list is empty
        if (app.locations.isNotEmpty()){
            emptyLayout.visibility = View.INVISIBLE
        }else{
            emptyLayout.visibility = View.VISIBLE
        }

        // FAB
        val fab : FloatingActionButton = root.findViewById(R.id.fab_add_location)
        fab.setOnClickListener {
            Toast.makeText(this.context, "NYI", Toast.LENGTH_SHORT).show()
        }

        // Inflate the layout for this fragment
        return root
    }
}

private class LocationAdapter(private var locations: ArrayList<Location>) : RecyclerView.Adapter<LocationAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAdapter.MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.list_card,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val location = locations[holder.adapterPosition]
        holder.bind(location)
    }

    override fun getItemCount(): Int = locations.size

    private class MainHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(location: Location){
            val cardTitle : TextView = itemView.findViewById(R.id.list_card_title)
            cardTitle.text = location.name
        }
    }
}