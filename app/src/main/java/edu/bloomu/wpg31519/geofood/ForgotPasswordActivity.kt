package edu.bloomu.wpg31519.geofood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var editEmail:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        mAuth = FirebaseAuth.getInstance()
        editEmail = findViewById(R.id.forgot_input)
    }

    fun resetPassword(view: View) {
        val email = editEmail.text.toString()
        if (email.isEmpty()) {
            editEmail.error = "Please Provide A Email"
            editEmail.requestFocus()
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.error = "Please Provide A Valid Email"
            editEmail.requestFocus()
        }else{
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this@ForgotPasswordActivity,"Reset password has " +
                            "been sent",Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(this@ForgotPasswordActivity,"Error Occurred",
                        Toast.LENGTH_LONG).show()
                }

            }
        }



    }
}