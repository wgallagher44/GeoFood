package edu.bloomu.wpg31519.geofood


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.icu.text.NumberFormat
import android.icu.util.Currency

import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng

import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider


/**
 * Main Activity for the geo food app. Geo Food is a location based app that prompts the
 * user for information about  price range and the radius in miles
 * and searches for all the food locations with those specifications
 * Known Bugs
 *
 * When restarting the tablet or turing off location it takes about a minute for the
 * tablet to determine your location to chek if your you have a location you can go into
 * google maps and wait till you have a blue dot of your location
 *
 */
class MainActivity : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    private lateinit var priceSlider: RangeSlider
    private lateinit var radiusSlider: Slider
    private lateinit var locationManager: LocationManager
    private lateinit var current: FusedLocationProviderClient
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    private var radius:Float = 10.0f
    var priceRangeLow = 0
    private var priceRangeHigh = 150

    companion object {
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val RADIUS = "radius"
        const val PRICERANGELOW = "price range low"
        const val PRICERANGEHIGH = "price range high"
        const val PERMISSION_REQUEST_ACCESS_LOCATION = 100

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val isEmpty = intent.extras?.getBoolean(ResultsActivity.isEmpty)
        if (isEmpty == true){
            createDialog("No Results Found")
        }

        priceSlider = findViewById(R.id.priceRange)
        radiusSlider = findViewById(R.id.radiusSlide)
        radiusSlider.setLabelFormatter { value ->
            String.format("%.2f miles", value)
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // updates the zoom of the map to fit the radius of the option circle and saves
        //the value into the radius variable
        radiusSlider.addOnChangeListener { _, rad, _ ->
            radius = rad

            if (rad < 9.0f && rad > 6.0f){
                moveMapInstant(11f)
            }else if (rad < 6.0f && rad >= 3.0f){
            moveMapInstant(12f)
            }else if (rad >= 9.0f && rad < 14.0f){
            moveMapInstant(10.5f)
            }else if (rad < 3.0f && rad >= 2.0f){
                moveMapInstant(13f)
            }else if (rad < 2.0f){
                moveMapInstant(14f)
            }else if (rad > 14.0f){
                moveMapInstant(10f)
            }
            deleteCircle()
            drawCircle(radius * 1609.34)


        }

        priceSlider.addOnChangeListener { slider, value, fromUser ->
            val values = priceSlider.values
            priceRangeLow = values[0].toInt()
            priceRangeHigh = values[1].toInt()

        }


//        if (!isNetworkAvailable()){
//            createDialog("No Internet Connection")
//        }


        //Adds a dollar sign in the range slider
        priceSlider.setLabelFormatter(LabelFormatter { value ->
            val currencyFormat = NumberFormat.getCurrencyInstance()
            currencyFormat.currency = Currency.getInstance("USD")
            currencyFormat.format(value.toDouble())
        })
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager



        //Prompts the user to enable there location services in higher api devices it
        //lest you pick between the fine and coarse location
        requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )


        current = LocationServices.getFusedLocationProviderClient(this)
        //checks if the location is available if it is not a dialog box appears
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            createDialog("Location Is Unavailable")
//            return
//        }
        //sets the first location of the map
//        current.lastLocation.addOnCompleteListener { loc ->
//            if (loc.result != null) {
//                latitude = loc.result.latitude
//                longitude = loc.result.longitude
           }






    /**
     * Zooms in or out by the zoom variable where a smaller number is more zoomed out
     * than a bigger number
     */
    private fun moveMap(zoom:Float) {
        val latLng = LatLng(latitude, longitude)

        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .draggable(true)
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom),
            3000,null)

    }
    private fun moveMapInstant(zoom: Float){
        val latLng = LatLng(latitude, longitude)

        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .draggable(true)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))

    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }

    /**
     * Creates the Menu Bar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_bar,menu)
        return true
    }

        /**
         * Searches for the food location based on the information the user imputed and
         * checks if the user has their location enabled for the app and launches them
         * to the loading activity
         */
        fun search(item: MenuItem) {
            if (!isNetworkAvailable()){
                createDialog("No Internet Connection")
                return
            }

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                createDialog("Location Is Unavailable")
                return
            }


//            current.lastLocation.addOnCompleteListener { location ->
////                if (location.result != null) {
////                    latitude = location.result.latitude
////                    longitude = location.result.longitude

                    val intent = Intent(this, LoadingActivity::class.java)
                    intent.putExtra(LATITUDE, latitude)
                    intent.putExtra(LONGITUDE, longitude)
                    intent.putExtra(PRICERANGELOW, priceRangeLow)
                    intent.putExtra(PRICERANGEHIGH, priceRangeHigh)
                    intent.putExtra(RADIUS, radius)
                    startActivity(intent)

              //  } else{
                 //   createDialog("Location Is Unavailable" )
              //  }
            //}
        }


    /**
     * initializes the google maps when the map is ready to be created and able to click
     * and add markers with radius circles
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        val location = LatLng( 39.085558,-103.944515)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,5f))
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMapClickListener{
            mMap.clear()
            latitude = it.latitude
            longitude = it.longitude
            mMap.addMarker(MarkerOptions().position(it).draggable(false))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it,10.5f
            ),3000, null)
            drawCircle(radius * 1609.34)
        }
    }

    /**
     * deletes the current circle on the map
     */
    private fun deleteCircle(){
        mMap.clear()
        moveMap(mMap.cameraPosition.zoom)
    }

    /**
     * creates the radius circle on the google map
     */
    private fun drawCircle(radius:Double){
        val circle = CircleOptions()

        val location = LatLng(latitude,longitude)
        circle.center(location)
        circle.strokeColor(Color.BLUE)
        circle.strokeWidth(2F)
        circle.radius(radius)
        mMap.addCircle(circle)
    }

    /**
     * creates a dialog box the has an error message
     */
    private fun createDialog(message:String){
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setMessage(message)

        val alertDialog =  alertBuilder.create()
        alertDialog.show()
    }

    /**
     * Checks if the tablet is connected to the internet
     */
    private fun isNetworkAvailable():Boolean{
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

                return true
            }
        }
        return false


    }

    /**
     * Gives a description about GeoFood
     */
    fun aboutGeoFood(item: MenuItem){
        createDialog("GeoFood is a location based app that helps you locate restaurants "+
                "in your area. Just put in a price range and radius and press the map " +
                "icon " +
                "to search for restaurants ")
    }

    /**
     * places a pin marker on the google map at your current location and it zooms into
     * that location
     */
    fun getCurrentLocation(item:MenuItem){

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        current.lastLocation.addOnCompleteListener {
            if (it.result != null){
                latitude = it.result.latitude
                longitude = it.result.longitude
                moveMap(10.5f)
                drawCircle(radius*1609.34)

            }else{
                createDialog("Location Not Available")
            }
        }
    }

}





