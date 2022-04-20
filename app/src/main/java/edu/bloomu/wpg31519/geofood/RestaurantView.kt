package edu.bloomu.wpg31519.geofood

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.view.LayoutInflater

import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide


/**
 *
 * @author Will Gallagher
 */
class RestaurantView(context: Context) : FrameLayout(context) {
    private var restaurantPicture:ImageView
    private var distance:TextView
    private var title:TextView
    private var ratingText:TextView
    private var icon:ImageButton
    private var ratingBar: RatingBar
    private var address:TextView


    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as
                LayoutInflater
        inflater.inflate(R.layout.restaurants,this)
        restaurantPicture = findViewById(R.id.restaurant_Image)
        distance = findViewById(R.id.distance)
        title = findViewById(R.id.restaurant_name)
        icon = findViewById(R.id.maps_icon)
        ratingBar = findViewById(R.id.ratingbar)
        ratingText = findViewById(R.id.ratingText)
        address = findViewById(R.id.address)



    }
    fun loadElements(
        restaurant: Restaurant,
        currentLatitude: Double,
        currentLongitude: Double
    ) {
        Glide.with(this).load(
            "https://maps.googleapis.com/maps/api/place/photo" +
                    "?maxwidth=" + 300 +
                    "&photo_reference=" + restaurant.getLink() +
                    "&key=AIzaSyDn_CqnqNuY9Z1l0-giVyAbByfii_UXFnA"
        ).into(restaurantPicture)
        val ratingFormat = "(${restaurant.getRating()})"
        ratingText.text = ratingFormat
        ratingBar.rating = restaurant.getRating()
        val currentLocation = Location("Current Location")
        currentLocation.longitude = currentLatitude
        currentLocation.longitude = currentLongitude
        val restaurantLocation = Location("End Location")
        restaurantLocation.longitude = restaurant.getLongitude()
        restaurantLocation.latitude = restaurant.getLatitude()
        val distance = currentLocation.distanceTo(restaurantLocation)
        val formattedDistance =
            String.format("%.2f mi", distance)
        this.distance.text = formattedDistance
        var formatString = ""
        val name = restaurant.getName().split(" ")
        val splitLine = name.size / 2
        for (i in name.indices) {
            formatString += "${name.get(i)} "
            if (i == splitLine && i >= 3) {
                formatString += "\n"
                formatString += "${name.get(i)} "
            }
        }
        title.text = formatString
        address.text = restaurant.getAddress()
        icon.setOnClickListener{view ->
            val formattedAddress = restaurant.getAddress().replace(" "
                , "+")
            val addressUrl = Uri.parse("google.navigation:q=$formattedAddress")
            val mapIntent = Intent(Intent.ACTION_VIEW,addressUrl)
            mapIntent.setPackage("com.google.android.apps.maps")
           context.startActivity(mapIntent)





        }


    }


}