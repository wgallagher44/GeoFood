package edu.bloomu.wpg31519.geofood

import android.content.Intent
import android.os.Bundle

import android.view.Menu

import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader

import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LoadingActivity : AppCompatActivity() {
    lateinit var locations: String
     var pageToken = ""




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
    private val apiKey = "AIzaSyDn_CqnqNuY9Z1l0-giVyAbByfii_UXFnA"
    var textView:TextView ?= null
    private var job:CompletableJob = Job()


    //Browser test: https://maps.googleapis.com/maps/api/place/nearbysearch/
     //json?keyword=restaurant&location=41.003510%2C-76.457650&radius=1000&key=AIzaSyDn_CqnqNuY9Z1l0-giVyAbByfii_UXFnA


    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        textView = TextView(this@LoadingActivity)

        radius = (intent.extras!!.getInt(MainActivity.RADIUS) * 1609.34).toFloat()
        latitude = intent.extras!!.getDouble(MainActivity.LATITUDE)
        longitude = intent.extras!!.getDouble(MainActivity.LONGITUDE)
        priceLow = intent.extras!!.getInt(MainActivity.PRICERANGELOW) - 1
        priceHigh = intent.extras!!.getInt(MainActivity.PRICERANGEHIGH) - 1
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "//keyword=restaurant&location=$latitude%2C$longitude" +
                "&radius=$radius" +
                "&type=restaurant" +
                "&operational=true" +
                  "&opennow=true" +
                "&minprice=$priceLow" +
                "&maxprice=$priceHigh" +
                "&key=AIzaSyDn_CqnqNuY9Z1l0-giVyAbByfii_UXFnA"




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
                delay(1000)
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

            val intent = Intent(this@LoadingActivity,
                        ResultsActivity::class.java)
            intent.putExtra(JSON,jsonList)
            intent.putExtra(LATITUDE,latitude)
            intent.putExtra(LONGITUDE,longitude)
            startActivity(intent)



        }




    }



    private suspend fun getRequest(placesUrl: String):String{

        val result = withContext(Dispatchers.IO + job) {
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

}
