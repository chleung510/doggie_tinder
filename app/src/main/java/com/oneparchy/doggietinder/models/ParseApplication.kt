package com.oneparchy.doggietinder.models

import android.app.Application
import com.oneparchy.doggietinder.R
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseUser

class ParseApplication : Application() {
    override fun onCreate() {
        //TODO: Setup parse & establish API keys
        super.onCreate()
        // Register parse models BEFORE initialize
        ParseObject.registerSubclass(Post::class.java)
        ParseObject.registerSubclass(Message::class.java)
        ParseUser.registerSubclass(User::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build())
    }
}