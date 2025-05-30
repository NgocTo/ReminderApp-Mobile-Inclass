package com.example.week3

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var homeButton: ImageButton
    private lateinit var profileButton: ImageButton

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
}