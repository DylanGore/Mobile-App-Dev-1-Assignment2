package ie.dylangore.mad1.assignment2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.dylangore.mad1.assignment2.R
import ie.dylangore.mad1.assignment2.main.MainApp
import ie.dylangore.mad1.assignment2.models.Warning
import org.jetbrains.anko.AnkoLogger

class WarningsFragment : Fragment(), AnkoLogger {

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

        val root : FrameLayout = inflater.inflate(R.layout.fragment_warnings, container, false) as FrameLayout
        val layoutManager = LinearLayoutManager(this.context)
        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view_warnings)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = WarningAdapter(app.warnings)

        // Inflate the layout for this fragment
        return root
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
            val card : CardView = itemView.findViewById(R.id.list_card)
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