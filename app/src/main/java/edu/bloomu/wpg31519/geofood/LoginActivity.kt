package edu.bloomu.wpg31519.geofood

import android.content.Intent
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


    private lateinit var mAuth: FirebaseAuth
    private lateinit var editUserName:EditText
    private lateinit var editPassword:EditText
    companion object{
        const val USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        editPassword = findViewById(R.id.password)
        editUserName = findViewById(R.id.email  )
    }

    fun login(view: View) {
        val username = editUserName.text.toString()
        val password = editPassword.text.toString()
        if (username.isEmpty()){
            editUserName.error = "Please enter a username"
            editUserName.requestFocus()
        }
        if (password.isEmpty()){
            editPassword.error = "Please enter a password"
            editPassword.requestFocus()
        }

        mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener {
            if (it.isSuccessful){
                val fireBaseUser = mAuth.currentUser
                val intent = Intent(this@LoginActivity,MainActivity::class.java)
                intent.putExtra(USER,fireBaseUser)
                startActivity(intent)
            }else{
                Toast.makeText(this@LoginActivity, "Invalid Username or Password"
                    ,Toast.LENGTH_LONG).show()
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
        menuInflater.inflate(R.menu.forgot_password,menu)
        return true
    }
}