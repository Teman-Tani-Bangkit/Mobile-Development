package com.dicoding.temantani.db

import android.content.Context

internal class UserPreference(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    fun setUserLogin(value : UserAuth){
        editor.putString(NAME, value.name)
        editor.putString(ID, value.id)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUser() : UserAuth {
        val auth = UserAuth()
        auth.name = preferences.getString(NAME, "")
        auth.id = preferences.getString(ID, "")
        auth.token = preferences.getString(TOKEN, "")

        return auth
    }

    fun userLogout(){
        editor.clear().apply()
    }

    companion object{
        private const val PREFS_NAME = "user_auth_pref"
        private const val NAME = "name"
        private const val ID = "id"
        private const val TOKEN = "token"
    }
}
