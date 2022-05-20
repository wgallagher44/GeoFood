package edu.bloomu.wpg31519.geofood

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * creates a splash screen that last for 2.5 seconds and has bounce animation.Also
 * launches the main activity
 */
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        actionBar?.hide()
        val icon = findViewById<ImageView>(R.id.logo_icon)
        val bounceAnimation = AnimationUtils.loadAnimation(applicationContext,R.anim.pin_bounce)
        icon.startAnimation(bounceAnimation)

        lifecycleScope.launch{
            delay(2500)
            val i = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            startActivity(i)
            // close this activity
            finish()
        }
    }
}