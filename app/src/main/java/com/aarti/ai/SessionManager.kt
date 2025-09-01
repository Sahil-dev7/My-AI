package com.aarti.ai

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val PREF_NAME = "AartiAppPrefs"
    private val IS_LOGGED_IN = "isLoggedIn"
    private val USER_EMAIL = "userEmail"

    private var prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = prefs.edit()

    fun setLogin(isLoggedIn: Boolean, email: String? = null) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn)
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGGED_IN, false)
    }

    fun getUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}

