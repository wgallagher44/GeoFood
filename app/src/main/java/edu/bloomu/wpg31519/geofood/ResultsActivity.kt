package edu.bloomu.wpg31519.geofood

import android.content.Intent

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.widget.TableLayout

import org.json.JSONObject

class ResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val json = intent.extras?.getStringArrayList(LoadingActivity.JSON)
        val latitude = intent.extras!!.getDouble(LoadingActivity.LATITUDE)
        val longitude = intent.extras!!.getDouble(LoadingActivity.LONGITUDE)
        val restaurants:ArrayList<Restaurant> = ArrayList()
        for (i in 0 until json!!.size){
            val jsonObject = JSONObject(json[i])
            val results = jsonObject.getJSONArray("results")
            for(j in 0 until results.length()){
                val rating  = results.getJSONObject(j).getDouble("rating")
                val address = results.getJSONObject(j).getString("vicinity")
                val name = results.getJSONObject(j).getString("name")
                val photoReference = results.getJSONObject(j).getJSONArray("photos")
                    .getJSONObject(0).getString("photo_reference")
                val location = results.getJSONObject(i).getJSONObject("geometry")
                    .getJSONObject("location")
                val lat = location.getDouble("lat")
                val long = location.getDouble("lng")
                val restaurant = Restaurant(name,address,lat,long,rating.toFloat()
                    ,photoReference)
                restaurants.add(restaurant)
            }
        }
        val tableLayout = findViewById<TableLayout>(R.id.table_layout)
        for (i in 0 until restaurants.size){
            val restaurantView = RestaurantView(this)
            restaurantView.loadElements(restaurants[i],latitude,longitude)
            tableLayout.addView(restaurantView)

        }





    }


}