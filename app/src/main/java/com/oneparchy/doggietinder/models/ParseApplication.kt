package com.oneparchy.doggietinder.models

import android.app.Application
import com.oneparchy.doggietinder.R
import com.parse.Parse
import com.parse.ParseObject

class ParseApplication : Application() {
    override fun onCreate() {
        //TODO: Setup parse & establish API keys
        super.onCreate()

        // Register parse models BEFORE initialize
        ParseObject.registerSubclass(Post::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("Bu8Jf3Z9qK30UJ9FWJJFeel5Cbteq6STeMOJeyBb")
                .clientKey("5xCJpAZiOvxMYsDprEvSSbzP1j5VbEhVTYPXLtr7")
                .server(getString(R.string.back4app_server_url))
                .build())
    }
}