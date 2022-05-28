package edu.bloomu.wpg31519.geofood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.TableLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class SavedActivity : AppCompatActivity() {

    lateinit var user:FirebaseUser

    companion object {
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val isRefresh = "Refresh"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        val restaurantList= intent.extras!!.getSerializable(MainActivity.RESTAURANTS) as ArrayList<Restaurant>
        val keyList = intent.extras!!.getStringArrayList(MainActivity.KEYS)
        user = FirebaseAuth.getInstance().currentUser!!

        val tableLayout = findViewById<TableLayout>(R.id.table_layout_saved)
        for(i in 0 until restaurantList.size){
            val restaurantView = SavedRestaurantView(this)
           restaurantView.loadElements(restaurantList[i],user,tableLayout)
            restaurantView.setBackgroundResource(R.drawable.boarder)
            tableLayout.addView(restaurantView)
    }
    }
    /**
     * inflates the action bar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.saved,menu)
        return true
    }
    fun toHome(item: MenuItem) {
        val intent = Intent(this@SavedActivity,MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}