package com.example.week3

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import com.example.week3.model.Reminder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random


/**
 * A simple [Fragment] subclass.
 * Use the [EntryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EntryFragment : Fragment() {

    private var listener: OnReminderCreatedListener? = null

    interface OnReminderCreatedListener {
        fun onReminderCreated(reminder: Reminder)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnReminderCreatedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnReminderCreatedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleInput = view.findViewById<EditText>(R.id.entryTxt)
        val descInput = view.findViewById<EditText>(R.id.descTxt)
        val urgentToggle = view.findViewById<ToggleButton>(R.id.urgentBtn)
        val confirmBtn = view.findViewById<Button>(R.id.setBtn)

        confirmBtn.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val description = descInput.text.toString().trim()
            val isUrgent = urgentToggle.isChecked

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val reminder = Reminder(
                id = Random.nextInt(1000, 9999).toString(),
                title = title,
                description = description,
                dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                isUrgent = isUrgent
            )

            // send data to MainActivity to update the RecyclerView
            listener?.onReminderCreated(reminder)
            // Scrub form clear
            titleInput.text.clear()
            descInput.text.clear()
            urgentToggle.isChecked = false

            Toast.makeText(requireContext(), "Reminder added!", Toast.LENGTH_SHORT).show()
        }
    }
}