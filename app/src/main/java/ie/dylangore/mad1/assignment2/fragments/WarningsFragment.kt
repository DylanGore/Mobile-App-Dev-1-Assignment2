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
import ie.dylangore.mad1.assignment2.databinding.FragmentWarningsBinding
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.Warning
import ie.dylangore.mad1.assignment2.services.WarningRequestService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * The fragment that displays the list of weather warnings
 */
class WarningsFragment : Fragment(), AnkoLogger {

    private lateinit var app: MainApp
    private lateinit var receiver : BroadcastReceiver

    // View binding
    private var _binding: FragmentWarningsBinding? = null
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
                if(intent?.hasExtra("warning")!!){
                    val warnings = intent.extras?.getParcelableArrayList<Warning.WarningItem>("warning")
                    updateWarningsList(warnings as ArrayList<Warning.WarningItem>)
                    info("list: $warnings")
                    info("WARNING Broadcast")
                    binding.layoutWarningsRefresh.isRefreshing = false
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWarningsBinding.inflate(inflater, container, false)

        // Update the RecyclerView initially with an empty list to avoid an error in the logs
        updateWarningsList()

        // Get the latest warnings
        refreshWarnings()

        // Handle the pull to refresh action
        binding.layoutWarningsRefresh.setOnRefreshListener {
            refreshWarnings()
            Toast.makeText(this.context,"Refreshed Warning List", Toast.LENGTH_SHORT).show()
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
     * Updates the displayed warning list
     */
    private fun updateWarningsList(warningsList: ArrayList<Warning.WarningItem> = arrayListOf()){
        val recyclerView = binding.recyclerViewWarnings
        val emptyLayout = binding.layoutEmptyWarnings

        // Setup the RecyclerView and Adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = WarningAdapter(warningsList)

        // Display a TextView if the list is empty
        if (warningsList.isNotEmpty()){
            emptyLayout.visibility = View.INVISIBLE
        }else{
            emptyLayout.visibility = View.VISIBLE
        }
    }

    /**
     * Call a background service to send a GET request to update
     * the list of warnings.
     */
    private fun refreshWarnings(){
        val intent = Intent(this.activity, WarningRequestService::class.java)
        WarningRequestService.enqueueWork(this.requireContext(), intent)
    }
}

/**
 * Adapter to handle displaying a list of weather warnings
 */
private class WarningAdapter(private var Warnings: ArrayList<Warning.WarningItem>) : RecyclerView.Adapter<WarningAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.list_card,
                parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val warning = Warnings[holder.adapterPosition]
        holder.bind(warning)
    }

    override fun getItemCount(): Int = Warnings.size

    private class MainHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(Warning: Warning.WarningItem){
            //val card : CardView = itemView.findViewById(R.id.list_card)
            val cardTitle : TextView = itemView.findViewById(R.id.list_card_title)
            val cardDescription : TextView = itemView.findViewById(R.id.list_card_description)
            // TODO modify color based on warning level
            //card.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.primaryColor))
            cardTitle.text = Warning.headline
            cardDescription.text = Warning.description
            cardDescription.visibility = View.VISIBLE
        }
    }
}