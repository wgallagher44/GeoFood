package edu.bloomu.wpg31519.geofood

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.json.JSONException
import org.json.JSONObject

/**
 * Shows the results from the http request and parse the json
 * retrieving the rating,name, address,photo reference, price level and latitude and
 * longitude of the restaurant
 */
class ResultsActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")

    companion object{
      const val isEmpty = "Is Empty"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"

    }
    private  var user:FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val json = intent.extras?.getStringArrayList(LoadingActivity.JSON)
        val latitude = intent.extras!!.getDouble(LoadingActivity.LATITUDE)
        val longitude = intent.extras!!.getDouble(LoadingActivity.LONGITUDE)
       user = FirebaseAuth.getInstance().currentUser
        val restaurants:ArrayList<Restaurant> = ArrayList()



            //parsing the json file
            for (i in 0 until json!!.size) {
                val jsonObject = JSONObject(json[i])
                val results = jsonObject.getJSONArray("results")
                for (j in 0 until results.length()) {
                    var rating = 0.0
                    if (results.getJSONObject(j).has("rating")){
                        rating = results.getJSONObject(j).getDouble("rating")
                    }
                    var address = ""
                    if (results.getJSONObject(j).has("vicinity")){
                        address = results.getJSONObject(j).getString("vicinity")
                    }
                    var name = ""
                    if (results.getJSONObject(j).has("name")){
                        name = results.getJSONObject(j).getString("name")
                    }

                    var photoReference = ""
                    photoReference = try {
                        results.getJSONObject(j).getJSONArray("photos")
                            .getJSONObject(0).getString("photo_reference")
                    }catch (e:JSONException){
                        ""
                    }
                    var priceLevel = 0
                    if(results.getJSONObject(j).has("price_level")) {
                        priceLevel = results.getJSONObject(j).getInt("price_level")

                    }


                    val restaurant = Restaurant(
                        name, address, rating.toFloat(), photoReference,priceLevel,""
                    )
                    restaurants.add(restaurant)
                }
            }
            //launches the main activity if there is no results from the api
            if(restaurants.isEmpty()) {
                val intent = Intent(applicationContext,MainActivity::class.java)
                intent.putExtra(isEmpty,true)
                intent.putExtra(LONGITUDE,longitude)
                intent.putExtra(LATITUDE,latitude)

                startActivity(intent)
                finish()

            }
            //adds restaurant values to compound component and adds the component to the
        // table layout
            val tableLayout = findViewById<TableLayout>(R.id.table_layout)

            for (i in 0 until restaurants.size) {
                val restaurantView = RestaurantView(this)
                restaurantView.loadElements(restaurants[i],user)
                restaurantView.setBackgroundResource(R.drawable.boarder)
                tableLayout.addView(restaurantView)
            }
    }

    /**
     * inflates the action bar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home,menu)
        return true
    }

    /**
     * launches back to the main activity
     */
    fun goHome(item: MenuItem) {
        val intent = Intent(this@ResultsActivity,MainActivity::class.java)
        startActivity(intent)
        finish()
    }



}