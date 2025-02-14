package com.oneparchy.doggietinder.models

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("_User")
class User: ParseUser() {
    companion object {
        const val KEY_DESC = "description"
    }

    fun getDescription(): String? {
        return getString(KEY_DESC)
    }
}