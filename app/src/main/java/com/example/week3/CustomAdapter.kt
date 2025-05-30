package com.example.week3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.week3.model.Reminder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CustomAdapter(private val dataSet: List<Reminder>?) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idTextView: TextView = view.findViewById(R.id.idDisplay)
        val titleTextView: TextView = view.findViewById(R.id.titleDisplay)
        val descTextView: TextView = view.findViewById(R.id.descDisplay)
        val dateTimeTextView: TextView = view.findViewById(R.id.dateDisplay)
        val urgentView: ImageView = view.findViewById(R.id.urgentDisplay)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val reminder = dataSet?.get(position)
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (reminder != null) {
            viewHolder.idTextView.text = reminder.id
            viewHolder.titleTextView.text = reminder.title
            viewHolder.descTextView.text = reminder.description

            // Parse the dateTime string to LocalDateTime
            val dateTime = LocalDateTime.parse(reminder.dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

            // Format datetime for display
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")
            viewHolder.dateTimeTextView.text = dateTime.format(formatter)

            // Show or hide urgent icon
            viewHolder.urgentView.visibility = if(reminder.isUrgent) View.VISIBLE else View.INVISIBLE
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet?.size ?: 0

}