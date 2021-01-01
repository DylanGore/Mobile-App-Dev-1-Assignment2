package ie.dylangore.mad1.assignment2.ui.fragments

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
import ie.dylangore.mad1.assignment2.activities.AddLocationActivity
import ie.dylangore.mad1.assignment2.databinding.FragmentLocationsBinding
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.Location
import org.jetbrains.anko.AnkoLogger

class LocationsFragment : Fragment(), AnkoLogger {

    lateinit var app: MainApp;
    private var _binding: FragmentLocationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = requireActivity().application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)

        updateLocations()

        // FAB
        binding.fabAddLocation.setOnClickListener {
            val intent = Intent(this.activity, AddLocationActivity::class.java)
            this.activity?.startActivityForResult(intent, 0)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        updateLocations()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun updateLocations(){
        val recyclerView = binding.recyclerViewLocations
        val emptyLayout = binding.layoutEmptyLocations

        // Setup the RecyclerView and Adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = LocationAdapter(app.locations)

        // Display a TextView if the list is empty
        if (app.locations.isNotEmpty()){
            emptyLayout.visibility = View.INVISIBLE
        }else{
            emptyLayout.visibility = View.VISIBLE
        }
    }

    /**
     * Required to fix possible memory leak when using view binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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