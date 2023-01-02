package com.app.sportify.utils

import android.content.Context
import android.util.Log
import com.app.networking.model.user.AppRole
import com.app.networking.model.user.UserAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserManager @Inject constructor(@ApplicationContext context: Context) {
    val sharedPreferences = context.getSharedPreferences("user_auth", Context.MODE_PRIVATE)

    fun saveUserAuth(userAuth: UserAuth) {
        val editor = sharedPreferences.edit()
        editor.putString("age", userAuth.age)
        editor.putString("gendre", userAuth.gendre)
        editor.putInt("id", userAuth.id)
        editor.putString("username", userAuth.username)
        try {
            val appRolesJson = Gson().toJson(userAuth.appRoles)
            editor.putString("role", appRolesJson)
        } catch (e: Exception) {
            Log.d("Exception", e.message.toString())
        }
        editor.apply()
    }

    fun getUserAuth(): UserAuth {
        val age = sharedPreferences.getString("age", "")
        val gendre = sharedPreferences.getString("gendre", "")
        val id = sharedPreferences.getInt("id", 0)
        val username = sharedPreferences.getString("username", "")
        val userRoleJson = sharedPreferences.getString("role", "")
        val appRolesListType = object : TypeToken<List<AppRole>>() {}.type
        val appRoles: List<AppRole> = Gson().fromJson(userRoleJson, appRolesListType)
        return UserAuth(
            age = age.toString(),
            gendre = gendre.toString(),
            id = id,
            username = username.toString(),
            appRoles = appRoles
        )
    }
}