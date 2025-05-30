package com.example.week3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var rememberCheck: CheckBox

    private val usernameTest = "user"
    private val passwordTest = "123"

    private val prefsKey = "MY_SHARED_PREFS"
    private val rememberStateKey = "CHECK_STATE"
    private val savedUsernameKey = "SAVED_USERNAME"
    private val authenticatedKey = "AUTHENTICATED"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set vars
        rememberCheck = findViewById(R.id.rememberField)
        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val sharedPrefs = getSharedPreferences(prefsKey, MODE_PRIVATE)

        // Autologin
        val authenticated = sharedPrefs.getBoolean(authenticatedKey, false)
        if (authenticated) {
            navigateHome()
        }

        // Remember me checkbox
        val isChecked = sharedPrefs.getBoolean(rememberStateKey, false)

        rememberCheck.isChecked = isChecked
        if (isChecked) {
            val savedUsername = sharedPrefs.getString(savedUsernameKey, "")
            usernameField.setText(savedUsername)
        }
        rememberCheck.setOnCheckedChangeListener { btn, newCheckState ->
            sharedPrefs.edit {
                putBoolean(rememberStateKey, newCheckState)
            }
        }

        // Login
        loginBtn.setOnClickListener {
            // Navigate to new activity
            if (usernameField.text.toString() == usernameTest && passwordField.text.toString() == passwordTest) {
                sharedPrefs.edit {
                    putBoolean(authenticatedKey, true)
                    putString(savedUsernameKey, usernameField.text.toString())
                }
                navigateHome()
            } else {
                Toast.makeText(this, "Check info and try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}