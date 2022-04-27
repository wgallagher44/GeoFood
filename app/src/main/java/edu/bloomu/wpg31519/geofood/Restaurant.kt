package edu.bloomu.wpg31519.geofood

import kotlin.properties.Delegates

/**
 * Holder for the food values accessed through the google places api this class holds
 * values of a restaurant including the name, address, latitude, longitude,rating and
 * a next page token
 *
 * @author Will Gallagher
 */
class Restaurant
/**
 * Initializes the name, address, latitude, longitude and rating of the restaurant
 */(
    private var name: String,
    private var address: String,
    private var rating: Float,
    private var photoLink : String,
    private var priceLevel : Int
) {

    /**
     * Returns the address of the restaurant
     */
    fun getAddress():String{
        return this.address
    }

    /**
     * gets the price level of the restaurant
     */
    fun getPriceLevel():Int{
        return priceLevel
    }

    /**
     * Returns the name of the restaurant
     */
    fun getName():String{
        return this.name
    }

    /**
     * gets the rating of the restaurant
     */
    fun getRating(): Float{
        return rating
    }

    /**
     * gets the photo reference
     */
    fun getLink():String{
        return photoLink
    }
}