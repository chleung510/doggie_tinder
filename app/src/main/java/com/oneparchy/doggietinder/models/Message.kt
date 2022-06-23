package com.oneparchy.doggietinder.models

import com.oneparchy.doggietinder.TimeFormatter
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser


/*
Required fields:
    Message: String
    Post: Post
    Image: File
    User: User
 */

@ParseClassName("Message")
class Message : ParseObject(){
    companion object {
        const val KEY_POST = "post"
        const val KEY_MSG = "msg"
        const val KEY_USERIMG = "userImg"
        const val KEY_USR = "user"
    }

    //Get/Set Image
    fun getUserImage(): ParseFile? {
        return getParseFile(KEY_USERIMG)
    }
    fun setUserImage(image: ParseFile) {
        put(KEY_USERIMG, image)
    }
    //Get/Set User
    fun getUser(): ParseUser? {
        return getParseUser(KEY_USR)
    }
    fun setUser(user: ParseUser) {
        put(KEY_USR, user)
    }

    fun getPost(): ParseObject? {
        return getParseObject(KEY_POST)
    }
    fun setPost(post: ParseObject) {
        put(KEY_POST, post)
    }


    //Get/Set Description
    fun getMessage(): String? {
        return getString(KEY_MSG)
    }
    fun setMessage(message: String) {
        put(KEY_MSG, message)
    }

    fun getFormattedTimestamp(): String? {
        return TimeFormatter.getTimeDifference(createdAt.toString())
    }
}