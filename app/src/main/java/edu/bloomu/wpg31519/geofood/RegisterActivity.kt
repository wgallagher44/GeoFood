package edu.bloomu.wpg31519.geofood

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth:FirebaseAuth
    private var editFirstName: EditText? = null
    private var editLastName: EditText?  = null
    private var editEmail: EditText?  = null
    private var editUsername: EditText?  = null
    private var editPassword: EditText?  = null
    private var editConfirmPassword: EditText?  = null
   private lateinit var registerButton: Button
    companion object{
        const val USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()

        editFirstName = findViewById(R.id.first_name)
        editLastName = findViewById(R.id.last_name)
        editEmail = findViewById(R.id.register_email)

        editPassword = findViewById(R.id.register_password)
        editConfirmPassword = findViewById(R.id.confirm_password)
        registerButton = findViewById(R.id.register)
        registerButton.setOnClickListener {
            val email = editEmail!!.text.toString()
            val firstName = editFirstName!!.text.toString()
            val lastName = editLastName!!.text.toString()
            val password = editPassword!!.text.toString()

            val confirmPassword = editConfirmPassword!!.text.toString()

            if (password != confirmPassword){
                editConfirmPassword!!.error = "Passwords Do Not Match"
                editConfirmPassword!!.requestFocus()
            }
             if (firstName.isEmpty()) {
                editFirstName!!.error = "Please Provide A First Name"
                editFirstName!!.requestFocus()
            }
            if (lastName.isEmpty()) {
                editLastName!!.error = "Please Provide A Last Name"
                editLastName!!.requestFocus()
            }

            if (email.isEmpty()) {
                editEmail!!.error = "Please Provide A Email"
                editEmail!!.requestFocus()
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editEmail!!.error = "Please Provide A Valid Email"
                editEmail!!.requestFocus()
            }
            if (password.length <= 5){
                editPassword!!.error = "Password must be longer than 5 characters "
                editPassword!!.requestFocus()
            }
            var isCapital = false
            for (i in password.indices){
                if (password[i].code < 91){
                    isCapital = true
                }
            }
             if (!isCapital){
                editPassword!!.error = "Password must contain an uppercase letter"
                editPassword!!.requestFocus()
            }
           else{
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = User(firstName, lastName,email)
                        val userId = task.result!!.user!!.uid
                        val database = Firebase.database
                        val ref = database.getReference(userId)
                        ref.setValue(user)
                        Toast.makeText(this@RegisterActivity, "User Has Been Created",
                            Toast.LENGTH_LONG).show()
                    }
                    val user = mAuth.currentUser
                    val intent = Intent(this@RegisterActivity,MainActivity::class.java)
                    intent.putExtra(USER,user)
                    startActivity(intent)

                }
        }


        }

    }



    fun resetFields(item: MenuItem) {
        editPassword!!.setText("")
        editConfirmPassword!!.setText("")
        editFirstName!!.setText("")
        editLastName!!.setText("")
        editEmail!!.setText("")
        editUsername!!.setText("")

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.reset,menu)
        return true
    }
}




