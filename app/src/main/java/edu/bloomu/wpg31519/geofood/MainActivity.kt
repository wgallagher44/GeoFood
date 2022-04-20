package edu.bloomu.wpg31519.geofood


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import java.text.NumberFormat
import java.util.*

/**
 * Main Activity for the geo food app. Geo Food is a location based app that prompts the
 * user for information about what type of food there price range and the radius in miles
 * and searches for all the food locations with those specifications
 */
class MainActivity : AppCompatActivity() {
    private lateinit var priceSlider: RangeSlider
    private lateinit var radiusSlider:Slider
    private lateinit var locationManager:LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var current :FusedLocationProviderClient
    private lateinit var location:Location
    var latitude : Double = 0.0
    var longitude : Double = 0.0
    var radius = 5
    var priceRangeLow = 0
    private var priceRangeHigh = 0
    companion object{
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val RADIUS = "radius"
        const val PRICERANGELOW = "price range low"
        const val PRICERANGEHIGH = "price range high"
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        priceSlider = findViewById(R.id.priceRange)
        radiusSlider = findViewById(R.id.radiusSlide)
        radiusSlider.setLabelFormatter { value ->
            String.format("%.2f miles", value)
        }

        radiusSlider.addOnChangeListener{ _, rad, _ ->
            radius = rad.toInt()
        }
        priceSlider.addOnChangeListener{slider, value, fromUser->
            val values = priceSlider.values
           priceRangeLow = values[0].toInt()
            priceRangeHigh = values[1].toInt()

        }


            //Those are the satrt and end values of sldier when user start dragging



//       priceRangeLow =  priceSlider.values[0].toInt()
//        priceRangeHigh = priceSlider.values[1].toInt()
        println(priceRangeHigh)
        //Adds a dollar sign in the range slider
//        priceSlider.setLabelFormatter(LabelFormatter { value ->
//            val currencyFormat = NumberFormat.getCurrencyInstance()
//            currencyFormat.currency = Currency.getInstance("USD")
//            currencyFormat.format(value.toDouble())
//        })
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener {location ->
            latitude =  location.latitude
            longitude =  location.longitude
        }







        //Prompts the user to enable there location services
        requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION
                ,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION)

        current = LocationServices.getFusedLocationProviderClient(this)


    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }


    /**
     * Searches for the food location based on the information the user imputed and
     * checks if the user has their location enabled for the app
     */
    fun search(view: View) {


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this,"Please Enable Your Location In Settings",
                Toast.LENGTH_SHORT).show()
            return
        }




        current.lastLocation.addOnCompleteListener { location ->
                if (location.result != null){
                    latitude = location.result.latitude
                    longitude = location.result.longitude

                    val intent = Intent(this,LoadingActivity::class.java)
                    intent.putExtra(LATITUDE,latitude)
                    intent.putExtra(LONGITUDE,longitude)
                    intent.putExtra(PRICERANGELOW, priceRangeLow)
                    intent.putExtra(PRICERANGEHIGH, priceRangeHigh)
                    intent.putExtra(RADIUS,radius)
                    startActivity(intent)

                }else{
                    locationManager.requestLocationUpdates(LocationManager
                        .NETWORK_PROVIDER,0,0f,locationListener)
                    Toast.makeText(this,"$latitude $longitude",Toast.LENGTH_LONG).show()
                }
        }
            }

    }





