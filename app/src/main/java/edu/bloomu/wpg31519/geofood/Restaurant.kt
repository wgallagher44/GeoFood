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
    private var latitude: Double,
    private var longitude: Double,
    private var rating: Float,
    private var photoLink : String
) {

    /**
     * Returns the address of the restaurant
     */
    fun getAddress():String{
        return this.address
    }

    /**
     * Returns the name of the restaurant
     */
    fun getName():String{
        return this.name
    }

    /**
     * Returns the latitude of the restaurant
     */
    fun getLatitude():Double{
        return this.latitude
    }
    /**
     * Returns the rating of the restaurant
     */
    fun getLongitude():Double{
        return this.longitude
    }
    fun getRating(): Float{
        return rating
    }
    fun getLink():String{
        return photoLink
    }

    override fun toString(): String {
        return "$name $photoLink \n"
    }
}