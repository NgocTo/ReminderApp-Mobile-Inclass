package com.example.week3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.edit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private val prefsKey = "MY_SHARED_PREFS"
    private val savedUsernameKey = "SAVED_USERNAME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val sharedPrefs = requireActivity().getSharedPreferences(prefsKey, Context.MODE_PRIVATE)
        val savedUsername = sharedPrefs.getString(savedUsernameKey, "Guest")

        // Update the greeting
        val greetingText = view.findViewById<TextView>(R.id.greetingTxt)
        val greeting = getString(R.string.greeting, savedUsername)
        greetingText.text = greeting

        // Logout
        val logoutBtn = view.findViewById<Button>(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            sharedPrefs.edit {
                clear()
            }
            // Clear file out
            requireContext().openFileOutput("MyReminders.txt", Context.MODE_PRIVATE).use {
                it.write("".toByteArray())
            }

            // Send user back
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // prevent back
            startActivity(intent)
        }

        return view
    }

}