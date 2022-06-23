package com.oneparchy.doggietinder.models

import com.oneparchy.doggietinder.TimeFormatter
import com.parse.*


/*
Required fields:
    Description: String
    Image: File
    User: User
 */
@ParseClassName("Post")
class Post : ParseObject(){

    companion object {
        const val KEY_DESC = "description"
        const val KEY_IMG = "image"
        const val KEY_USR = "user"

        const val KEY_DOGNAME = "nameOfDog"
        const val KEY_SEX = "sex"
        const val KEY_AGE = "age"
        const val KEY_BREED = "breed"

        const val KEY_LOCATION = "location"
        const val KEY_CITY = "cityName"
        const val KEY_FOUND = "isFound"
    }

    //Get/Set Image
    fun getImage(): ParseFile? {
        return getParseFile(KEY_IMG)
    }
    fun setImage(image: ParseFile) {
        put(KEY_IMG, image)
    }
    //Get/Set User
    fun getUser(): ParseUser? {
        return getParseUser(KEY_USR)
    }
    fun setUser(user: ParseUser) {
        put(KEY_USR, user)
    }

    /* For getting and setting lost dog's information.*/
    fun getDogName(): String? {
        return getString(KEY_DOGNAME)
    }

    fun setDogName(nameOfDog: String) {
        put(KEY_DOGNAME, nameOfDog)
    }

    fun getSex(): String? {
        return getString(KEY_SEX)
    }

    fun setSex(sex: String) {
        put(KEY_SEX, sex)
    }

    fun getAge(): String? {
        return getString(KEY_AGE) + " yrs old"
    }

    fun setAge(age: String) {
        put(KEY_AGE, age)
    }

    fun getBreed(): String? {
        return getString(KEY_BREED)
    }

    fun setBreed(breed: String) {
        put(KEY_BREED, breed)
    }

    //Get/Set Description
    fun getDescription(): String? {
        return getString(KEY_DESC)
    }
    fun setDescription(description: String) {
        put(KEY_DESC, description)
    }

    fun getLocation(): ParseGeoPoint? {
        return getParseGeoPoint(KEY_LOCATION)
    }

    fun setLocation(location: ParseGeoPoint) {
        put(KEY_LOCATION, location)
    }

    fun getCity(): String? {
        return getString(KEY_CITY)
    }

    fun setLocation(city: String) {
        put(KEY_CITY, city)
    }

    fun isFound(): Boolean? {
        return getBoolean(KEY_FOUND)
    }

    fun setFound(t: Boolean){
        put(KEY_FOUND,t)
    }

    fun getFormattedTimestamp(): String? {
        return TimeFormatter.getTimeDifference(createdAt.toString())
    }

}