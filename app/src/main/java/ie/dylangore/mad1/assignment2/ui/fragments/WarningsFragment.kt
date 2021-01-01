package ie.dylangore.mad1.assignment2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.databinding.FragmentWarningsBinding
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.Warning
import org.jetbrains.anko.AnkoLogger

class WarningsFragment : Fragment(), AnkoLogger {

    lateinit var app: MainApp;
    private var _binding: FragmentWarningsBinding? = null
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
        _binding = FragmentWarningsBinding.inflate(inflater, container, false)

        val recyclerView = binding.recyclerViewWarnings
        val emptyLayout = binding.layoutEmptyWarnings

        // Setup the RecyclerView and Adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = WarningAdapter(app.warnings)

        // Display a TextView if the list is empty
        if (app.warnings.isNotEmpty()){
            emptyLayout.visibility = View.INVISIBLE
        }else{
            emptyLayout.visibility = View.VISIBLE
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
}

private class WarningAdapter(private var Warnings: ArrayList<Warning.WarningItem>) : RecyclerView.Adapter<WarningAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarningAdapter.MainHolder {
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