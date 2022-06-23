package com.oneparchy.doggietinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

const val USER_EXTRA = "USER_EXTRA"
class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Check if a user is logged in first. If yes, go straight to Main Activity
        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }

        //Click handler for login button
        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val username = findViewById<EditText>(R.id.etUsername).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()
            loginUser(username,password)
        }

        //Click handler for signup button
        findViewById<Button>(R.id.btnSignup).setOnClickListener {
            val username = findViewById<EditText>(R.id.etUsername).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()
            signupUser(username,password)
        }
    }

    private fun signupUser(username: String, password: String) {
        //Create the ParseUser
        val user = ParseUser()

        //Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        //Sign up the user with the entered credentials
        user.signUpInBackground { e ->
            if (e == null) {
                // User has signed up successfully
                Log.i(TAG,"User signup successful")
                Toast.makeText(this, "Welcome to Doggie Tinder!", Toast.LENGTH_SHORT).show()
                goToMainActivity()
            } else {
                // Sign up failed. Look at the ParseException to figure out what went wrong
                e.printStackTrace()
                Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Log in the user with the entered credentials
    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                Log.i(TAG, "Login success!")
                goToMainActivity()
            } else {
                e.printStackTrace()
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        } ) )
    }

    //Go to Main activity
    private fun goToMainActivity() {
        val i = Intent(this@LoginActivity, MainActivity::class.java)
        val user = ParseUser.getCurrentUser()
        i.putExtra(USER_EXTRA, user)
        startActivity(i)
        finish()
    }
}