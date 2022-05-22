package edu.bloomu.wpg31519.geofood

import android.content.Intent
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem


import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Request an http request from the google places near by search api with a coroutine.
 * While the app is preforming the http request in the background it shows a loading
 * circle that will spin indefinitely till the it is launched to the Results activity
 */
class LoadingActivity : AppCompatActivity() {

    companion object{
        const val JSON = "json"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"


    }
    private  var radius:Float = 0.0f
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0
    private var priceLow:Int = 0
    private var priceHigh:Int = 0
    private var apiKey = ""







    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        apiKey = resources.getString(R.string.apiKey)
        //Retrieve values from the main activity
        //1609.34 is the constant to convert miles to meters which is used for the api
        // instead of miles
        radius = (intent.extras!!.getFloat(MainActivity.RADIUS) * 1609.34).toFloat()
        latitude = intent.extras!!.getDouble(MainActivity.LATITUDE)
        longitude = intent.extras!!.getDouble(MainActivity.LONGITUDE)
        priceLow = intent.extras!!.getInt(MainActivity.PRICERANGELOW)
        priceHigh = intent.extras!!.getInt(MainActivity.PRICERANGEHIGH)



        //converts the minimum/maximum price into price levels where 1 is > 10
        //2 is between 10 and 25 exclusive 3 = 25 - 50 exclusive and 4 if the price is
        //greater than 50
        val newLowPrice = when{
            priceLow <= 10 ->1
            priceLow in 11..24 -> 2
            priceLow in 25..50 -> 3
            priceLow > 50 -> 4
            else->0
        }
        val newHighPrice =when{
            priceHigh <= 10 ->1
            priceHigh in 11..24 -> 2
            priceHigh in 25..45 -> 3
            priceHigh > 50 -> 4
            else->0
        }



        //api url request with value from the main activity
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "//keyword=restaurant&location=$latitude%2C$longitude" +
                "&radius=$radius" +
                "&type=restaurant" +
                "&operational=true" +
                  "&opennow=true" +
                "&minprice=$newLowPrice" +
                "&maxprice=$newHighPrice" +
                "&key=$apiKey"



        // makes an http request on the io thread which is used for database calls and
        // http request and saves them as json represented as strings
        lifecycleScope.launch {
           val jsonList:ArrayList<String> = ArrayList()
            val result = getRequest(url)
            jsonList.add(result)
            val json  = JSONObject(result)
            var pageToken = ""
            if (json.has("next_page_token")){
                 pageToken = json.get("next_page_token").toString()
            }
            while (pageToken != "") {
                delay(1000) // page token isn't instantly valid so I delayed till
                //it is valid
                val newUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                        "json?&pagetoken=$pageToken&key=$apiKey"
                val nextResult = getRequest(newUrl)
                jsonList.add(nextResult)
                val nextJson = JSONObject(nextResult)
                if (nextJson.has("next_page_token")){
                    pageToken = nextJson.getString("next_page_token")
                }else{
                    pageToken = ""
                }
            }
            //launches a new page and sends the json strings
            val intent = Intent(this@LoadingActivity,
                        ResultsActivity::class.java)
            intent.putExtra(JSON,jsonList)
            intent.putExtra(LATITUDE,latitude)
            intent.putExtra(LONGITUDE,longitude)

            finish()

            startActivity(intent)
        }
    }


    /**
     * Does an http request on the IO thread and saves the json as a string
     */
    private suspend fun getRequest(placesUrl: String):String{

        val result = withContext(Dispatchers.IO) {
            val inputStream: InputStream
            // create URL
            val url: URL = URL(placesUrl)

            // create HttpURLConnection
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

            // make GET request to the given URL
            conn.connect()

            // receive response as inputStream
            inputStream = conn.inputStream

            // convert inputstream to string
            if (inputStream != null)
                convertInputStreamToString(inputStream)
            else
                "Did not work!"


        }
        return result
    }

    /**
     * reads the input stream line by line and converts it to a string
     */
    private fun convertInputStreamToString(inputStream: InputStream): String {
        val bufferedReader: BufferedReader? = BufferedReader(InputStreamReader(inputStream))

        var line:String? = bufferedReader?.readLine()
        var result:String = ""

        while (line != null) {
            result += line
            line = bufferedReader?.readLine()
        }

        inputStream.close()
        return result
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.loading,menu)
        return true
    }


    fun home(item: MenuItem) {
        lifecycleScope.cancel()
        val intent = Intent(this@LoadingActivity,MainActivity::class.java)
        startActivity(intent)
    }


}
