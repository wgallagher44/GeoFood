package edu.bloomu.wpg31519.geofood

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    private lateinit var mAuth: FirebaseAuth
    private lateinit var editUserName: EditText
    private lateinit var editPassword: EditText
    private  var isLogout:Boolean = false
    companion object {


        const val USERNAME = "username"
        const val PASSWORD = "password"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        isLogout = intent.extras?.getBoolean(MainActivity.isLogout) == true
        if (isLogout){
            sharedPreferences.edit().clear().apply()
        }
        mAuth = FirebaseAuth.getInstance()
        editPassword = findViewById(R.id.password)
        editUserName = findViewById(R.id.email)

        val username = sharedPreferences.getString(USERNAME, "")
        val password = sharedPreferences.getString(PASSWORD, "")
        if (username != "" && password != "") {
            if (username != null) {
                if (password != null) {
                    mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }



    }
    fun login(view: View) {
        val username = editUserName.text.toString()
        val password = editPassword.text.toString()
        if (username.isEmpty()) {
            editUserName.error = "Please enter a username"
            editUserName.requestFocus()
        }
        if (password.isEmpty()) {
            editPassword.error = "Please enter a password"
            editPassword.requestFocus()
        }

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val editor = sharedPreferences.edit()
                editor.putString(USERNAME, username)
                editor.putString(PASSWORD,password)
                editor.apply()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(
                    this@LoginActivity, "Invalid Username or Password", Toast.LENGTH_LONG
                ).show()
            }


        }


    }

    fun register(view: View) {
        val register = Intent(applicationContext, RegisterActivity::class.java)
        startActivity(register)

    }

    fun continueAsGuest(view: View) {
        val main = Intent(applicationContext, MainActivity::class.java)
        startActivity(main)
        finish()


    }

    fun forgotPassword(item: MenuItem) {
        val forgotPassword = Intent(applicationContext, ForgotPasswordActivity::class.java)
        startActivity(forgotPassword)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.forgot_password, menu)
        return true
    }
}