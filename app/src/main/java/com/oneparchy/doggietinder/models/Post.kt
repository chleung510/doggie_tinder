package com.oneparchy.doggietinder.models

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

/*
Required fields:
    Description: String
    Image: File
    User: User
 */
@ParseClassName("Post")
class Post : ParseObject() {

    companion object {
        const val KEY_DESC = "description"
        const val KEY_IMG = "image"
        const val KEY_USR = "user"
    }

    //Get/Set Description
    fun getDescription(): String? {
        return getString(KEY_DESC)
    }
    fun setDescription(description: String) {
        put(KEY_DESC, description)
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

}