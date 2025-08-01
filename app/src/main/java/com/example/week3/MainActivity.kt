package com.example.week3

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week3.model.Reminder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(),  EntryFragment.OnReminderCreatedListener {
    private lateinit var homeButton: ImageButton
    private lateinit var profileButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter

    private var reminderList: MutableList<Reminder>? = null
    private val fileName = "MyReminders.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        homeButton = findViewById(R.id.homeBtn)
        profileButton = findViewById((R.id.profileBtn))

        // Setup RecyclerView
        recyclerView = findViewById(R.id.remindersRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        reminderList = loadReminders().toMutableList()
        adapter = CustomAdapter(
            reminderList,
            onEmailClick = { reminder -> onEmailClicked(reminder) },
            onDeleteClick = { reminder -> onDeleteClicked(reminder) }
        )
        recyclerView.adapter = adapter

        // inflate default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, EntryFragment())
            .commit()

        homeButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, EntryFragment())
                .commit()
            updateButtonTint(homeButton)
        }
        profileButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ProfileFragment())
                .commit()

            updateButtonTint(activeButton = profileButton)
        }

    }
    private fun updateButtonTint(activeButton: ImageButton) {
        val skyBlue = getColor(R.color.sky_blue)
        val black = getColor(R.color.black)
        activeButton.imageTintList = ColorStateList.valueOf(skyBlue)

        val buttons = listOf(homeButton, profileButton)
        buttons.filter {
            it != activeButton
        }.forEach {
            it.imageTintList = ColorStateList.valueOf(black)
        }
    }

    override fun onReminderCreated(reminder: Reminder) {
        reminderList?.add(0, reminder)
        adapter.notifyItemInserted(0)
        recyclerView.scrollToPosition(0)

        saveReminders()
    }

    private fun saveReminders() {
        val json = Gson().toJson(reminderList)

        openFileOutput(fileName, MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    private fun loadReminders(): List<Reminder> {
        return try {
            val file = File(filesDir, fileName)
            if (!file.exists()) return emptyList()

            val json = file.readText()
            val type: Type = object : TypeToken<List<Reminder>>() {}.type
            Gson().fromJson<List<Reminder>>(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun onEmailClicked(reminder: Reminder) {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")
        val formattedDate = LocalDateTime.parse(reminder.dateTime).format(formatter)

        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        selectorIntent.setData("mailto:".toUri())

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("n_to239383@fanshaweonline.ca"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reminder: ${reminder.title}")
        emailIntent.putExtra(Intent.EXTRA_TEXT,
            reminder.description +
            "\n\nCreated: $formattedDate" +
            "\n Category: ${reminder.category}" +
            "\n Urgent? ${if (reminder.isUrgent) "Yes" else "No"}")

        emailIntent.selector = selectorIntent
        startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    private fun onDeleteClicked(reminder: Reminder) {
        val index = reminderList?.indexOf(reminder) ?: -1
        if (index != -1) {
            reminderList?.removeAt(index)
            adapter.notifyItemRemoved(index)
            saveReminders()
        }
    }
}