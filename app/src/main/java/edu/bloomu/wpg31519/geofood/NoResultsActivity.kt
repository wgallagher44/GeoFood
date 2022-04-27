package edu.bloomu.wpg31519.geofood

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

class NoResultsActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_results)
        val noResultsText = findViewById<TextView>(R.id.noResults)
        noResultsText.text =
            "No Restaurants within that radius and price range please press the home " +
                    "icon in the top right and change your radius or price range."
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.no_results_home,menu)
        return super.onCreateOptionsMenu(menu)

    }
    fun homePage(item:MenuItem){
        val intent = Intent(this@NoResultsActivity,MainActivity::class.java)
        startActivity(intent)
    }
}