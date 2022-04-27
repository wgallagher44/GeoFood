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
 * loads restaurant the photo reference into an image view, restaurant title,rating value
 * and address into a text view and the rating into a rating bar and routing icon into a
 * image button.
 * @author Will Gallagher
 */
class RestaurantView(context: Context) : FrameLayout(context) {
    private var restaurantPicture:ImageView
    private var title:TextView
    private var ratingText:TextView
    private var icon:ImageButton
    private var ratingBar: RatingBar
    private var address:TextView
    private var priceLevel:TextView

    /**
     * Initializes all the text views, rating bar, image view and image button with the id
     * from the restaurant.xml
     */
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as
                LayoutInflater
        inflater.inflate(R.layout.restaurants,this)
        restaurantPicture = findViewById(R.id.restaurant_Image)

        title = findViewById(R.id.restaurant_name)
        icon = findViewById(R.id.maps_icon)
        ratingBar = findViewById(R.id.ratingbar)
        ratingText = findViewById(R.id.ratingText)
        address = findViewById(R.id.address)
        priceLevel = findViewById(R.id.price)
    }

    /**
     * sets all of the xml components from the restaurant values
     */
    fun loadElements(
        restaurant: Restaurant,
    ) {
            if (restaurant.getLink() != "") {
                Glide.with(this).load(
                    "https://maps.googleapis.com/maps/api/place/photo" +
                            "?maxwidth=" + 300 +
                            "&photo_reference=" + restaurant.getLink() +
                            "&key=AIzaSyDn_CqnqNuY9Z1l0-giVyAbByfii_UXFnA"
                ).into(restaurantPicture)
            }


        val ratingFormat = "(${restaurant.getRating()})"
        ratingText.text = ratingFormat
        ratingBar.rating = restaurant.getRating()

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

        priceLevel.text = when{
            restaurant.getPriceLevel() == 1 -> "Inexpensive\n >$10"
            restaurant.getPriceLevel() == 2 -> "Moderately Expensive\n $10-$25"
            restaurant.getPriceLevel() == 3 -> "Expensive\n 25$-$45"
            restaurant.getPriceLevel() == 4 -> "Very Expensive\n <$50"
            else->""
        }


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